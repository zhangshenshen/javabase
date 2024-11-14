package main.java.base.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("Server started on port " + port);

        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received message: " + message);
//        socket.close();
    }
}
