package main.java.base.socket.aio;

import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client implements CompletionHandler<Integer, Integer> {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    /**
     * 异步socket通道
     **/
    private AsynchronousSocketChannel asc;
    /**
     * 读写共用的缓冲区
     **/
    private ByteBuffer buf = ByteBuffer.allocate(1024);

    private FileInputStream fis;

    private FileChannel fc;
    /**
     * 待发送文件的最大编号
     **/
    private int maxFileNo = 3;

    private String IP = "127.0.0.1";


    public void start() throws IOException, ExecutionException,
            InterruptedException {
        /*
          创建socket 通道
         */
        asc = AsynchronousSocketChannel.open();
        /*
        建立连接，使用future 模式等待连接的建立
         */
        Future<Void> future = asc.connect(new InetSocketAddress(IP, 9000));
        //阻塞操作 连接成功才会继续
        future.get();
        log.info("Connected to {} success", asc.getRemoteAddress());
        this.send(0);
    }

    public void send(int i) throws IOException {
        StreamUtil.close(fc);
        StreamUtil.close(fis);

        String fn = String.format("s%d.txt", i);
        fis = new FileInputStream(fn);
        fc = fis.getChannel();
        buf.clear();
        buf.putLong(fc.size() + 8);
        //file channel date into buffer
        fc.read(buf);
        buf.flip();
        log.info("write first buffer of file {}", fn);
        //将buffer的内容写入socket channel，异步操作，这里直接返回。
        asc.write(buf, i, this);
        log.info("return in send method of write first buffer of file {}，time" +
                        " is {}",
                fn, LocalDateTime.now());
    }

    @Override
    public void completed(Integer result, Integer attachment) {
        if (result <= 0) {
            log.info("no written data now. quit");
            return;
        }
        log.info("written {} bytes ", result);

        try {
            if (buf.hasRemaining()) {
                asc.write(buf, attachment, this);
                return;
            }
//            也可以不发送剩余数据，把buffer compact之后，留出空间继续使用。
//            buf.compact();
            buf.clear();

            if (fc.read(buf) > 0) {
                //文件中还有数据没传完
                buf.flip();
                asc.write(buf, attachment, this);
            } else {
                //读不到说明已遇到文件尾，表示发送完成。这时可以读取服务端响应了
                //此操作也是阻塞的block 另外一个传入completionHandler接口的read方法才是非阻塞的
                result = asc.read(buf).get();
                log.info("read  response {} bytes, time is {}", result, LocalDateTime.now());
                buf.flip();
                long total = buf.getLong();
                long received = buf.getLong();
                System.out.printf("%d_%d%n", received, total);

                //发送下一个文件，这里也可以将文件集合的iterator作为附件;
                log.info("start send next file,if exist");
                if (attachment < maxFileNo) {
                    this.send(attachment + 1);
                } else {
                    asc.shutdownOutput();
                    //通知主线程继续执行（退出）
                    synchronized (this) {
                        this.notify();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error on send file", e);
            this.close();
        }

    }


    @Override
    public void failed(Throwable exc, Integer attachment) {
        this.close();
        SocketAddress sa = null;
        try {
            sa = asc.getRemoteAddress();
        } catch (IOException e) {
            log.error("Error on get remote address", e);
        }
        log.error("Error on read from {}", sa, exc);

    }

    private void close() {
        StreamUtil.close(asc);
        StreamUtil.close(fis);
        StreamUtil.close(fc);
    }

    public static void main(String[] args) {
//        BufferedReader br = null;
        Client client = new Client();
        try {
            client.start();
            //阻塞主线程，等待Client完成传输并唤醒自己
            log.info("client main thread start blocking, time is {}", LocalDateTime.now());
            synchronized (client) {
                client.wait();
            }
            log.info("client main thread end blocking, time is {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error on run client", e);
        } finally {
//            StreamUtil.close(br);
            client.close();
        }
        System.out.println("bye");
    }
}
