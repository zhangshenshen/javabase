package main.java.base.multithread.advance.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SemaphoreDemo.class);
    private static final Semaphore semaphore = new Semaphore(5, true);
    private static final SecureRandom random = new SecureRandom();

    @Override
    public void run() {
        log.info("acquire permit");
        try {
            semaphore.acquire();
            try {
                log.info("got permit and {} remain",semaphore.availablePermits());
                Thread.sleep(1000L + random.nextInt(2000));
            }finally {
                semaphore.release();
                log.info("release permit and {} remain",semaphore.availablePermits());
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            SemaphoreDemo demo = new SemaphoreDemo();
            es.execute(demo);
        }
        es.shutdown();
    }
}
