package main.java.base.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerDemo {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8889);
             Socket socket = server.accept();
             InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream();
             Scanner scanner = new Scanner(is)) {

            PrintWriter pw = new PrintWriter(os,true);
            pw.println("Hello  zss");
            boolean done = false;
            while (!done && scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
                if("1024".equalsIgnoreCase(scanner.nextLine())){
                    done = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
