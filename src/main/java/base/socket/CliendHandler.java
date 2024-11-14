package main.java.base.socket;

import java.io.*;
import java.net.Socket;


public class CliendHandler extends Thread {
    private Socket socket;

    public CliendHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printWriter = new PrintWriter(outputStream, true);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Received: " + line);
                printWriter.println("Server " + line);

            }
            socket.close();
        }catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }
}
