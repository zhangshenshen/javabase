package main.java.base.multithread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                int i1 = 0;
                while (!Thread.interrupted() && i1 < Integer.MAX_VALUE) {
                    i1++;
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10L, TimeUnit.SECONDS);
        System.out.println(System.currentTimeMillis() - l);


    }
}
