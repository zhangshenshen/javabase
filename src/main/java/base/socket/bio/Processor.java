package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Processor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Processor.class);


    private Socket socket;

    public Processor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (!Thread.interrupted()) {
                String s = in.readLine();
                System.out.printf("%s say %s%n",
                        socket.getRemoteSocketAddress(), s);
                out.println(s);
                if ("bye".equalsIgnoreCase(s)) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error on process command", e);
        } finally {
            StreamUtil.close(in);
            StreamUtil.close(out);
            StreamUtil.close(socket);
        }
    }
}
