package main.java.base.socket.aio;

import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Server implements CompletionHandler<AsynchronousSocketChannel,
        Object> {
    private static final Logger log = LoggerFactory.getLogger(Server.class);


    private AsynchronousServerSocketChannel assc = null;

    public void start() throws IOException {
        assc = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(9000));
        /*
        异步非阻塞接口。操作系统会在有新连接请求时，调用this的completed方法
         另外一个返回future的accept，如果调用future
         .get会阻塞住
        */
        log.info("Server started");
        assc.accept(null, this);
    }


    @Override
    public void completed(AsynchronousSocketChannel asc, Object attachment) {
        //继续受理一个连接,这里不阻塞，直接进入上一个连接的结果处理过程
        assc.accept(null, this);
        /*
        为当前通道准备一个reader对象来完成处理，而不是用一个线程来处理，
        此reader拥有一个buffer。并持有当前通道的引用
         */
        ReadCompletionHandler reader = new ReadCompletionHandler(asc);
        log.info("server completed connection ====start into read handler");
        //对于新建的连接，接受的文件在命名时，编号从0开始
        asc.read(reader.getBuffer(), 0, reader);

    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        this.close();
        log.error("Error on accept connection", exc);
    }

    private void close() {
        StreamUtil.close(assc);
    }

    public static void main(String[] args) {
        BufferedReader br = null;
        Server server = new Server();
        try {
            server.start();
            String cmd = null;
            System.out.println("Enter 'exit' to quit.'");
            br = new BufferedReader(new InputStreamReader(System.in));
            while ((cmd = br.readLine()) != null) {
                if ("exit".equalsIgnoreCase(cmd)) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error on run server", e);
        } finally {
            StreamUtil.close(br);
            server.close();
        }
        System.out.println("bye");
    }
}
