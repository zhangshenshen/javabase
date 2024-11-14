package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ByteClient {

    private final static Logger log = LoggerFactory.getLogger(ByteClient.class);
    public static void main(String[] args) {

        String filePath = "src/main/resources/static/test.csv";
        BufferedReader bufferedReader = null;
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Socket socket = null;
        byte[] bufferBytes = new byte[10240];
        try {
            socket = new Socket("127.0.0.1", 9000);
            bufferedOutputStream =
                    new BufferedOutputStream(socket.getOutputStream());
            bufferedInputStream =
                    new BufferedInputStream(new FileInputStream(new File(filePath)));

            int t = bufferedInputStream.available();
            int r = 0;
            while ((r = bufferedInputStream.read(bufferBytes)) > 0) {
                t -= r;
                log.info("Read {} bytes from the file. {} bytes remain.", r,
                        t);
                bufferedOutputStream.write(bufferBytes, 0, r);
                bufferedOutputStream.flush();
            }

            socket.shutdownOutput();

            bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = bufferedReader.readLine();
            if ("ok".equalsIgnoreCase(s)) {
                log.info("Transport the file successfully.");
            }

        } catch (IOException e) {
            log.error("Error on connection", e);
        } finally {
            StreamUtil.close(bufferedReader);
            StreamUtil.close(bufferedInputStream);
            StreamUtil.close(bufferedOutputStream);
            StreamUtil.close(socket);
        }
        System.out.println("done");
    }

}
