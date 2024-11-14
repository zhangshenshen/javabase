package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class MultiPartProcessor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MultiPartProcessor.class);

    private Socket socket;

    public MultiPartProcessor(Socket socket) {
        this.socket = socket;
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }


    @Override
    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        PrintWriter pw = null;
        byte[] buf = new byte[10240];
        int i = 0;
        byte[] header = new byte[4];


        try {
            bis = new BufferedInputStream(socket.getInputStream());
            pw = new PrintWriter(socket.getOutputStream(), true);
            while (!socket.isInputShutdown() && bis.read(header) == 4) {
                int t = byteArrayToInt(header), total = t, r = 0;
                if (t < 1) {
                    pw.println("error");
                    break;
                }
                log.info("Incoming file size= {}", t);
                String fn =
                        String.format("src/main/resources/static/files%d" +
                                ".txt", i);
                bos = new BufferedOutputStream(new FileOutputStream(fn));

                try {

                    while (t > 0 && (r = bis.read(buf)) > 0) {
                        t -= r;
                        log.debug("received {} bytes,{} bytes remaining", r, t);
                        bos.write(buf, 0, r);
                        bos.flush();
                    }
                    log.info("received {} bytes as file {}", total, fn);
                    pw.println("ok");
                    i++;
                } finally {
                    StreamUtil.close(bos);
                }
            }


        } catch (IOException e) {
            log.error("Error on transport", e);
        } finally {
            StreamUtil.close(bis);
            StreamUtil.close(pw);
            StreamUtil.close(socket);
        }

    }
}
