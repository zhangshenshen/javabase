package main.java.base.socket.chatroom;



import main.java.base.socket.bio.StreamUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Server implements Runnable {
    private Selector selector = null;
    private ServerSocketChannel ssc = null;
    private Thread thread = new Thread(this);
    private Queue<String> messageQueue = new ConcurrentLinkedQueue<String>();
    private volatile boolean live = true;

    public void start() throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(9000));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        thread.setName("Server Thread");
        thread.start();
    }

    public static void main(String[] args) {
        BufferedReader br = null;
        Server server = new Server();
        try {
            server.start();
            String cmd = null;
            System.out.println("enter 'exit' to exit.");
            br = new BufferedReader(new InputStreamReader(System.in));
            while ((cmd = br.readLine()) != null) {
                if (cmd.equals("exit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error on run server");
        } finally {
            StreamUtil.close(br);
            server.close();
        }
        System.out.println("bye");
    }


    @Override
    public void run() {

        try {
            while (live && !Thread.interrupted()) {
//                System.out.println("before select");
                if (selector.select(1000) == 0) {
//                    System.out.println("selector empty----");
                    continue;
                }
                ByteBuffer outBuffer = null;
                String outMsg = messageQueue.poll();
                if (outMsg != null) {
                    outBuffer = ByteBuffer.wrap(outMsg.getBytes());
                    outBuffer.limit(outMsg.length());
                }

                Set<SelectionKey> set = selector.selectedKeys();
//                System.out.println("key size :" +set.size());
                Iterator<SelectionKey> iterator = set.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isValid() && key.isAcceptable()) {
                        System.out.println("key accepted");
                        this.onAcceptable(key);
                    }
                    if (key.isValid() && key.isReadable()) {
                        System.out.println("key read");
                        this.onReadable(key);
                    }
                    if (key.isValid() && key.isWritable() && outBuffer != null) {
                        System.out.println("key write");
                        SocketChannel sc = (SocketChannel) key.channel();
                        this.write(sc, outBuffer);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error on run server");
        }
    }

    private void write(SocketChannel sc, ByteBuffer buffer) throws IOException {
        buffer.position(0);
        int r = 0;
        try {
            while (buffer.hasRemaining() && (r = sc.write(buffer)) > 0) {
//                log.info("write back {} bytes to {}", r, sc
//                .getRemoteAddress());
                System.out.println("write back " + r + sc.getRemoteAddress());
            }
        } catch (Exception e) {
//            log.error("Error on socket write", e);
            System.out.println("Error on socket write");
            sc.close();
            return;
        }
    }

    private void onReadable(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer inBuffer = (ByteBuffer) key.attachment();
        int r = 0;
        String rs = null;
        StringBuilder sb = new StringBuilder();

        try {
            while ((r = channel.read(inBuffer)) > 0) {
//                log.info("received {} bytes from {}", r,
//                        channel.getRemoteAddress());
                System.out.println("received {} bytes from {}" + r + channel.getRemoteAddress());

                inBuffer.flip();
                sb.append(StandardCharsets.UTF_8.decode(inBuffer));
                inBuffer.clear();
                rs = sb.toString();

                if (rs.endsWith("\n")) {
                    break;
                }
            }
        } catch (Exception e) {
//            log.error("Error on socket read", e);
            System.out.println("Error on socket read");
            StreamUtil.close(channel);
            return;
        }


        if (rs != null && !rs.isEmpty()) {
            String[] sa = rs.split("\n");
            for (String s : sa) {
                if (s != null && !s.isEmpty()) {
//                    log.info("address:{} send:{}", channel.getRemoteAddress()
//                            , s);
                    System.out.println("address:" + channel.getRemoteAddress() + " send:" + s);
                    messageQueue.add(String.format("%s:%s\n",
                            channel.getRemoteAddress(), s));
                    if ("bye".equalsIgnoreCase(s)) {
                        StreamUtil.close(channel);
                    }
                }
            }
        }

    }

    private void onAcceptable(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = null;
        try {
            sc = ssc.accept();

            if (sc != null) {
//                log.info("Accepted connection from {}", sc.getRemoteAddress
//                ());
                System.out.println("Accepted connection from " + sc.getRemoteAddress());
                sc.configureBlocking(false);
                sc.register(selector,
                        SelectionKey.OP_READ | SelectionKey.OP_WRITE,
                        ByteBuffer.allocate(1024));
            }
        } catch (IOException e) {
//            log.error("Accept failed", e);
            System.out.println("Accept failed");
            StreamUtil.close(sc);
        }
    }

    public void close() {
        live = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
//            log.error("Error on thread join", e);
            System.out.println("Error on thread join");
        }
        StreamUtil.close(ssc);
        StreamUtil.close(selector);

    }


}
