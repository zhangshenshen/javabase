package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ByteProcessor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ByteProcessor.class);


    private Socket socket;

    public ByteProcessor(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        PrintWriter pw = null;
        byte[] buf = new byte[10240];

        try {
            bis = new BufferedInputStream(socket.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(new File("src" +
                    "/main/resources/static/temp.csv")));
            pw = new PrintWriter(socket.getOutputStream(), true);
            int r = 0, t = 0;
            while ((!socket.isInputShutdown()) && (r = bis.read(buf)) > 0) {
                t += r;
                log.info("Received {}/{} bytes", r, t);
                bos.write(buf, 0, r);
                bos.flush();
            }
            log.info("received {} bytes and done", t);
            pw.println("ok");


        } catch (IOException e) {
            log.error("Error on transport", e);
        } finally {
            StreamUtil.close(bos);
            StreamUtil.close(bis);
            StreamUtil.close(socket);
            StreamUtil.close(pw);
        }

    }
}
