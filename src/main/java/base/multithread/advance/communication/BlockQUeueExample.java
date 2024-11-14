package main.java.base.multithread.advance.communication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class BlockQUeueExample {
    public static void main(String[] args) {

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);

// 生产者
        new Thread(() -> {
            try {
                queue.put("Data");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

// 消费者
        new Thread(() -> {
            try {
                String data = queue.take();
                System.out.println("Received data: " + data);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

    }
}
