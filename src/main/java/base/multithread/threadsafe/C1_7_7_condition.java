package main.java.base.multithread.threadsafe;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class C1_7_7_condition implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(C1_7_7_condition.class);

    private static final Lock lock = new ReentrantLock();

    private static final Condition condition1 = lock.newCondition();
    private static final Condition condition2 = lock.newCondition();
    private boolean flag;

    public C1_7_7_condition(boolean flag) {
        this.flag = flag;
    }


    @Override
    public void run() {
        lock.lock();

        try {
            if (flag) {
                log.info("before condition1 await");
                condition1.await();
                log.info("after condition1 await");
            }else {
                log.info("before condition2 await");
                condition2.await();
                log.info("after condition2 await");
            }
        }catch (InterruptedException e){
            log.info(e.getMessage());
        }finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new C1_7_7_condition(true));
        es.execute(new C1_7_7_condition(false));

        es.execute(new Runnable() {
            @Override
            public void run() {
                boolean b = true;
                lock.lock();
                try {
                    Thread.sleep(1000L);
                    log.info("condition1 signalAll");
                    condition1.signalAll();
                    lock.unlock();
                    Thread.sleep(1000L);
                    lock.lock();
                    if(b){
                        Thread.sleep(5000L);
                        log.info("condition2 signalAll");
                        condition2.signalAll();
                        return;
                    }
                }catch (InterruptedException e){
                    log.info(e.getMessage());
                }finally {
                    lock.unlock();
                }

                lock.lock();

                try {
                    Thread.sleep(5000L);
                    log.info("condition2 signalAll");
                    condition2.signalAll();
                }catch (InterruptedException e){
                    log.info(e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        });

        es.shutdown();

    }
}
