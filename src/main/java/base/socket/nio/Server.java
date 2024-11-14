package main.java.base.socket.nio;


import main.java.base.socket.bio.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


public class Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private Selector selector = null;
    private ServerSocketChannel ssc = null;
    private Thread thread = new Thread(this);
    private volatile boolean live = true;

    public void start() throws IOException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(9000));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        thread.start();
        System.out.println("server thread started");
    }


    @Override
    public void run() {
        try {
            while (live && !Thread.interrupted()) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isValid() && key.isAcceptable()) {
                        this.onAcceptable(key);
                    }

                    if (key.isValid() && key.isReadable()) {
                        this.onReadable(key);
                    }
                    if (key.isValid() && key.isWritable()) {
                        this.onWritable(key);
                    }
                }


            }

        } catch (IOException e) {
            log.error("Error on socket I/O", e);
        }

    }

    public void onAcceptable(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = null;
        try {
            sc = ssc.accept();
            if (sc != null) {
                log.info("client {} connected", sc.getRemoteAddress());
                sc.configureBlocking(false);
                sc.register(key.selector(), SelectionKey.OP_READ,
                        ByteBuffer.allocate(1024));

            }
        } catch (IOException e) {
            log.error("Error on accept connection", e);
            StreamUtil.close(sc);
        }
    }

    public void onReadable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();

        FileChannel fc = null;
        PrintWriter pw = null;

        try {
            InetSocketAddress isa = (InetSocketAddress) sc.getRemoteAddress();
            fc = new FileOutputStream(isa.getAddress().getHostAddress() + "_" + isa.getPort()).getChannel();
            pw = new PrintWriter(System.out, true);
            int r = 0;
            buf.clear();

            while ((r = sc.read(buf)) > 0) {
                log.info("Received {} bytes from {}", r, isa);
                buf.flip();
                pw.println(StandardCharsets.UTF_8.decode(buf));
                r = fc.write(buf);
                log.info("Written {} bytes to file", r);
                buf.clear();
            }
            sc.register(key.selector(), SelectionKey.OP_WRITE, "ok");
        } catch (IOException e) {
            log.error("Error on read socket", e);
            StreamUtil.close(sc);
        } finally {
            StreamUtil.close(fc);
        }
    }

    public void onWritable(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        String s = key.attachment().toString();
        try {
            byte[] ba = s.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buf = ByteBuffer.wrap(ba);
            buf.limit(ba.length);
            int r = 0;
            while (buf.hasRemaining() && (r = sc.write(buf)) > 0) {
                log.info("Written {} bytes to {}", r, sc.getRemoteAddress());
            }
        } catch (Exception e) {
            log.error("Error on write socket", e);
        } finally {
            StreamUtil.close(sc);
        }
    }

    public void close() {
        live = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("Be interrupted on thread join", e);
        }
        StreamUtil.close(selector);
        StreamUtil.close(ssc);

    }

    public static void main(String[] args) {
        BufferedReader br = null;
        Server server = new Server();
        try {
            server.start();
            String cmd = null;
            System.out.println("Enter 'exit' to exit");
            br = new BufferedReader(new InputStreamReader(System.in));
            while ((cmd = br.readLine()) != null) {
                if ("exit".equalsIgnoreCase(cmd)) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error on start server", e);
        } finally {
            StreamUtil.close(br);
            server.close();
        }
        System.out.println("bye");
    }


}
