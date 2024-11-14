package main.java.base.multithread.threadpool;

import java.util.concurrent.*;

public class TestExecutors {

    private static final ExecutorService executorService1 = Executors.newFixedThreadPool(10);
    private static final ExecutorService executorService2 = Executors.newCachedThreadPool();
    private static final ExecutorService executorService3 = Executors.newSingleThreadExecutor();
    private static final ExecutorService executorService4 = Executors.newScheduledThreadPool(10);
    private static final ExecutorService executorService5 = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    public static void main(String[] args) {

    }


}
