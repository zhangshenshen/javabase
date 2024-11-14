package main.java.base.socket.aio;

import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;

public class ReadCompletionHandler implements CompletionHandler<Integer,
        Integer> {
    private static final Logger log =
            LoggerFactory.getLogger(ReadCompletionHandler.class);

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private AsynchronousSocketChannel asc;
    private FileOutputStream fos;
    private FileChannel fc;

    private long total;
    private long received;

    public ReadCompletionHandler(AsynchronousSocketChannel asc) {
        this.asc = asc;
    }

    /**
     * @param result     The result of the I/O operation.
     *                   对于accept方法，这里返回的是socket通道，对于read
     *                   write方法，这里返回的是本次操作读取或写入的字节数
     * @param attachment The object attached to the I/O operation when it was
     *                   initiated. 附件是io操作发起时加入的内容 在accept read write方法都能传入
     */
    @Override
    public void completed(Integer result, Integer attachment) {
        if (result <= 0) {
            //当客户端调用 socket通道的shutdownOutput时就会进入这里
            log.info("no more incoming data now. quit");
            return;
        }
        received += result;
        log.info("read {}/{}/{} bytes", result, received, total);
        try {
            //buffer中已经填充好数据。只需要取出来处理。因为是异步的，进入这里说明数据已经填充好
            buffer.flip();
            //这里使用fc为空作为判断是否是一个新文件的依据，其实是根据文件大小total计算出来的，当接收够了上一个文件的数据，就重置了
            if (fc == null) {
                //首8字节记录了要传输的字节总数，包括此8字节
                total = buffer.getLong();
                //这里的 地址应该是只client的地址，
                InetSocketAddress isa =
                        (InetSocketAddress) asc.getRemoteAddress();
//                fos = new FileOutputStream(isa.getAddress().getHostAddress
//                ()+"/"+isa.getPort());
                fos = new FileOutputStream(String.format("%d_" + "%d.csv",
                        isa.getPort(), attachment));
                fc = fos.getChannel();
            }
            //把buffer中的数据写入文件通道。同步的，因为是append，这里buffer也被限制了大小，所以不会阻塞太长时间
            fc.write(buffer);

            buffer.clear();
            if (received < total) {
                //未读完继续读
                asc.read(buffer, attachment, this);
            } else {
                buffer.putLong(total);
                buffer.putLong(received);
                buffer.flip();
                //这里是一个阻塞操作
                result = asc.write(buffer).get();
                log.info("written response {} bytes", result);
                //重设reader
                reset();
                //读取下一个文件的数据？
                //太快了 等一下？
                asc.read(buffer, attachment + 1, this);
            }
        } catch (Exception e) {
            log.error("Error on receive file", e);
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
        this.close();
    }

    public ByteBuffer getBuffer() {
        log.info("read handler buffer====");
        return buffer;
    }

    public void reset() {
        //关闭正在写入的文件，准备读取一个新文件
        StreamUtil.close(fos);
        fos = null;
        StreamUtil.close(fc);
        //重要，新文件传输开始的标识
        fc = null;
        total = 0;
        received = 0;
    }

    public void close() {
        this.reset();
        StreamUtil.close(asc);
    }
}
