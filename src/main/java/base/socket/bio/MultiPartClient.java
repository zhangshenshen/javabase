package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;


public class MultiPartClient {
    private static final Logger log = LoggerFactory.getLogger(MultiPartClient.class);


    public static byte[] intToByteArray(int value) {
        return new byte[]{(byte) ((value >> 24) & 0xFF),
                (byte) ((value >> 16) & 0xFF), (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)};
    }


    public static void main(String[] args) {
        BufferedReader sr = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        byte[] buffer = new byte[10240];

        Socket socket = null;

        try {
            socket = new Socket("127.0.0.1", 9000);
            sr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bos = new BufferedOutputStream(socket.getOutputStream());

            for (int i = 0; i < 3; i++) {
                String fn =
                        String.format("src/main/resources/static/file%d" +
                                ".txt", i);
                bis = new BufferedInputStream(new FileInputStream(fn));

                try {
                    int t = bis.available(), r = 0;
                    log.info("Send file {},size = {}", fn, t);
                    bos.write(intToByteArray(t));

                    while ((r = bis.read(buffer)) > 0) {
                        t -= r;
                        log.debug("read {} bytes from,{} bytes remain", r, t);
                        bos.write(buffer, 0, r);
                        bos.flush();

                    }
                    String s = sr.readLine();
                    if ("ok".equalsIgnoreCase(s)) {
                        log.info("transport file {} successfully", fn);
                    } else {
                        log.error("transport file {} failed", fn);
                    }


                } finally {
                    StreamUtil.close(bis);
                }

            }
            socket.shutdownOutput();
        } catch (IOException e) {
            log.error("Error on connection", e);
        } finally {
            StreamUtil.close(sr);
            StreamUtil.close(bos);
            StreamUtil.close(socket);
        }
        System.out.println("done");

    }
}
