package main.java.base.multithread.threadsafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class C1_7_6_ReadWriteLock {

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger log = LoggerFactory.getLogger(C1_7_6_ReadWriteLock.class);
    private static Deque<Integer> data = new LinkedList<Integer>();
    private static int value;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        lock.writeLock().lock();
//                        lock.readLock().lock();

                        try {
                            data.addFirst(++value);
                            log.info("add-{}", value);
                        }finally {
                            lock.writeLock().unlock();
//                            lock.readLock().unlock();
                        }
                    }
                }
            });

            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        Integer v = null;
                        do {
                            lock.readLock().lock();
//                            lock.writeLock().lock();
                            try {
                                v = data.pollLast();
                                if(v == null){
                                    Thread.yield();
                                }else {
//                                    log.info("in while output-{}",v);
                                    break;
                                }
                            }finally {
                                lock.readLock().unlock();
//                                lock.writeLock().unlock();
                            }
                        }while (true);
                        log.info("outside output-{}",v);
                    }
                }
            });
        }

        es.shutdown();
        es.awaitTermination(10L, TimeUnit.SECONDS);



        log.info("real order");
        log.info("data size: {}", data.size());
        for (Integer i : data) {
            System.out.println(i);
        }
    }
}
