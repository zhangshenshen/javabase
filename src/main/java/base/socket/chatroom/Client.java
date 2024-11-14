package main.java.base.socket.chatroom;


import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Client implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);



    private Selector selector;
    private SocketChannel socketChannel;
    private Thread thread = new Thread(this);
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Queue<String> queue = new ConcurrentLinkedQueue<String>();
    private volatile boolean live = true;
    private String ip = "47.100.234.51";
//    private String ip = "127.0.0.1";


    public void start() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(ip, 9000));
        log.info("client try connecting server...");

        while (!socketChannel.finishConnect()) {

        }

        socketChannel.register(selector,
                SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        thread.start();
        log.info("client connected to server {}",
                socketChannel.getRemoteAddress());

    }


    @Override
    public void run() {

        log.info("client starting...");

        try {
            while (live && !Thread.interrupted()) {
                if (selector.select(1000) == 0) {
                    continue;
                }


                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    SocketChannel sc = null;
                    int r = 0;
                    String s = null;

                    if (key.isValid() && key.isReadable()) {
                        sc = (SocketChannel) key.channel();
                        StringBuilder sb = new StringBuilder();
                        buffer.clear();
                        while ((r = sc.read(buffer)) > 0) {
                            log.debug("Receive {} bytes from {}", r,
                                    sc.getRemoteAddress());
                            buffer.flip();
                            sb.append(StandardCharsets.UTF_8.decode(buffer));
                            buffer.clear();
                            s = sb.toString();
                            if (s.endsWith("\n")) {
                                break;
                            }
                        }

                        String[] sa = null;
                        if (s != null) {
                            sa = s.split("\n");
                        }
                        if (sa != null) {
                            for (String a : sa) {
                                if (a != null && !a.isEmpty()) {
                                    System.out.println(a);
                                }
                            }
                        }
                    }
                    if (key.isValid() && key.isWritable() && !queue.isEmpty()) {
                        s = queue.poll();
                        sc = (SocketChannel) key.channel();
                        ByteBuffer buf =
                                ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
                        buf.limit(s.length());
                        while (buf.hasRemaining() && ((r = sc.write(buf)) > 0)) {
                            log.debug("write {} bytes to server", r);
                        }
                    }

                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            StreamUtil.close(selector);
            StreamUtil.close(socketChannel);
        }

    }

    public void close() {
        live = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StreamUtil.close(selector);
        StreamUtil.close(socketChannel);
    }

    public boolean isLive() {
        return thread.isAlive();
    }

    public void send(String msg) {
        queue.add(msg);
    }

    public static void main(String[] args) {
        BufferedReader ir = null;
        Client client = new Client();

        try {
            client.start();
            String cmd = null;
            ir = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Say 'bye' to exit");
            while ((cmd = ir.readLine()) != null && client.isLive()) {
                if (!cmd.isEmpty()) {
                    client.send(cmd.concat("\n"));
                    if ("bye".equalsIgnoreCase(cmd)) {
                        client.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error on run client", e);
        } finally {
            StreamUtil.close(ir);
            client.close();
        }
        System.out.println("done");
    }

}
