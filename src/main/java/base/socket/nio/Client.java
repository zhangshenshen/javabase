package main.java.base.socket.nio;




import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;



public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);


    public static void main(String[] args) {


        SocketChannel sc = null;
        FileChannel fc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(true);
            sc.connect(new InetSocketAddress("127.0.0.1", 9000));
            if (!sc.finishConnect()) {
                log.error("Can not connect to server");
                return;
            }

            fc = new FileInputStream(new File("src/main/resources/static" +
                    "/file1.txt")).getChannel();
            ByteBuffer buf = ByteBuffer.allocate(10240);
            int r = 0;
            while ((r = fc.read(buf)) > 0) {
                log.info("Read {} bytes from file");
                buf.flip();
                while (buf.hasRemaining() && ((r = sc.write(buf)) > 0)) {
                    log.info("Write {} bytes to server", r);
                }
                buf.clear();
                log.info("end of read file");
            }

            while ((r = sc.read(buf)) > 0) {
                log.info("Read {} bytes from socket", r);
            }
            buf.flip();
            log.info(StandardCharsets.UTF_8.decode(buf).toString());
            log.info("end of read socket");

        } catch (IOException e) {
            log.error("client start fail",e);
        } finally {
            StreamUtil.close(fc);
            StreamUtil.close(sc);
        }
        System.out.println("done");


    }

}
