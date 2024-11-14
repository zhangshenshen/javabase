package main.java.base.socket.nio;


import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class C2_8_3_FileChannel {
    private static final Logger log = LoggerFactory.getLogger(C2_8_3_FileChannel.class);


    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fcr = null;
        FileChannel fcw = null;
        Charset charset = StandardCharsets.UTF_8;

        try {
            File f = new File("src/main/resources/static/files0.txt");
            fis = new FileInputStream(f);
            fos = new FileOutputStream(f, true);
            fcr = fis.getChannel();
            fcw = fos.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while (fcr.read(buf) > 0) {
                buf.flip();
                System.out.println(charset.decode(buf));
                buf.clear();
            }
            buf = charset.encode(LocalDateTime.now().toString() + "/n");
            while (buf.hasRemaining()) {
                fcw.write(buf);
            }
        } catch (IOException e) {
            log.error("Error on read file", e);
        } finally {
            StreamUtil.close(fcr);
            StreamUtil.close(fcw);
        }

    }

}
