package main.java.base.socket.bio;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {

        BufferedReader ir = null;
        BufferedReader sr = null;
        String cmd = null;
        PrintWriter pw = null;

        try (Socket socket = new Socket("127.0.0.1", 9000)) {
            sr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Say 'bye' to exit");

            ir = new BufferedReader(new InputStreamReader(System.in));

            while ((cmd = ir.readLine()) != null) {
                pw.println(cmd);
                String s = sr.readLine();
                System.out.printf("Server say %s%n", s);
                if ("bye".equalsIgnoreCase(s)) {
                    break;
                }
            }

        } catch (IOException e) {
            log.error("Error on connection", e);
        }
        System.out.println("Bye bye");
    }

}
