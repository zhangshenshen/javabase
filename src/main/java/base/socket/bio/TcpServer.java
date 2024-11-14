package main.java.base.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public static void main(String[] args) throws IOException {

        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("Server start");

            Socket socket = serverSocket.accept();
            System.out.println("client connected");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer))!=-1){
                String message = new String(buffer,0, bytesRead);
                System.out.println("receive from client:" + message);

                outputStream.write(("message received:"+message).getBytes());
            }

            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
