package main.java.base.multithread.threadsafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class C1_7_3_wait_notify implements Runnable {

    private static final byte[] flag = new byte[0];
    private static final Logger log = LoggerFactory.getLogger(C1_7_3_wait_notify.class);

    @Override
    public void run() {
        synchronized (flag) {
            try {
                log.info("before flag.wait");
                flag.wait();
                log.info("after flag.wait");

                Thread.sleep(1000L);
//                flag.wait(1000L);
                log.info("after thread sleep");
            }catch (InterruptedException e) {
                log.error("thread sleep interrupted{}", e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new C1_7_3_wait_notify());
        es.execute(new C1_7_3_wait_notify());
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (flag) {
                    log.info("flag notifyAll");
//                    flag.notifyAll();
                    flag.notify();
                }
            }
        });
        es.shutdown();
    }
}
