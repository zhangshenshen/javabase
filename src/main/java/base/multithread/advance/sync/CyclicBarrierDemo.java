package main.java.base.multithread.advance.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class CyclicBarrierDemo implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(CyclicBarrierDemo.class);
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    private int page;

    public CyclicBarrierDemo(int page) {
        this.page = page;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(1000L + page * 1000L);
                log.info("{} waiting threads",cyclicBarrier.getNumberWaiting());
                cyclicBarrier.await(10L, TimeUnit.SECONDS);
                log.info("continue...");
            }catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            CyclicBarrierDemo cb = new CyclicBarrierDemo(i);
            es.execute(cb);
        }
        es.shutdown();
    }
}
