package main.java.base.multithread.threadlocal;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalDemo {

    static class LocalVariable {
        private final Long[] a = new Long[1024*1024];
    }

    final static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,5,1,
            TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    final static ThreadLocal<LocalVariable> localVariableThreadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        for (int i = 0; i < 50; i++) {
            poolExecutor.execute(()->{
                localVariableThreadLocal.set(new LocalVariable());
                System.out.println(Thread.currentThread().getName()+"use local variable"  +localVariableThreadLocal.get());
                localVariableThreadLocal.remove();
            });
        }
        System.out.println("pool execute over!");
    }
}
