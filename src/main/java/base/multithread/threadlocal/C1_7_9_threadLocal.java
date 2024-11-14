package main.java.base.multithread.threadlocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class C1_7_9_threadLocal implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(C1_7_9_threadLocal.class);

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    @Override
    public void run() {
        log.info("start = {}",threadLocal.get());
        threadLocal.set(Thread.currentThread().getName());

        try{
            //
            test();
        }finally {
            threadLocal.remove();
        }


    }

    void test(){
        log.info("test = {}",threadLocal.get());
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            C1_7_9_threadLocal threadLocal = new C1_7_9_threadLocal();
            es.execute(threadLocal);
        }
        es.shutdown();
        try {
            es.awaitTermination(10L, TimeUnit.SECONDS);

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
