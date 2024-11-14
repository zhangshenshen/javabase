package main.java.base.multithread.advance.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        int count = 200;
        final CountDownLatch latch = new CountDownLatch(count);
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < count; i++) {
            es.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + "-" + latch.getCount());
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        try {
            latch.await(10L, TimeUnit.SECONDS);// block current thread until the counter minus to zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
        System.out.println(Thread.currentThread().getName() + latch.getCount());


    }

}
