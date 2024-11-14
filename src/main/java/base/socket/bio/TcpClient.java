package main.java.base.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 9000);
//        socket.connect(new InetSocketAddress("localhost", 9000));

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        String message = "hello world";

        outputStream.write(message.getBytes());

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            System.out.println("receive from server:" + new String(buffer, 0, bytesRead));
        }


    }
}
