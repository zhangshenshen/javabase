package main.java.base.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int port = 12345;
        InetAddress address = InetAddress.getByName(hostName);
        DatagramSocket socket = new DatagramSocket();

        String message = "Hello World Server";
        byte[] buf = message.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        System.out.println("Message sent");
        socket.close();

    }
}
