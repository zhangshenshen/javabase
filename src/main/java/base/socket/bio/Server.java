package main.java.base.socket.bio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private ServerSocket serverSocket = null;

    public void start() throws IOException {
        serverSocket = new ServerSocket(9000);
        threadPool.execute(this);
    }


    @Override
    public void run() {

        Socket socket = null;
        try {
            while ((socket = serverSocket.accept()) != null) {
                log.info("Client {} connected",
                        socket.getRemoteSocketAddress());
                threadPool.execute(new MultiPartProcessor(socket));
            }
        } catch (IOException e) {
            log.error("Error on process connection", e);
        }

    }

    public void close() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.error("Error on close server socket", e);
            }
        }
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        Server server = new Server();
        BufferedReader br = null;
        try {
            server.start();

            System.out.println("Enter 'exit' to exit.'");
            br = new BufferedReader(new InputStreamReader(System.in));
            String cmd = null;
            while ((cmd = br.readLine()) != null) {
                if ("exit".equalsIgnoreCase(cmd)) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error on run server", e);
        } finally {
            StreamUtil.close(br);
            server.close();
        }

    }

}
