package main.java.base.multithread.threadsafe;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo implements Runnable {

    private static final Set<Integer> dataSet = new HashSet<Integer>();
    private static int count;

    protected int page;

    public Demo(int page) {
        this.page = page;
    }

    @Override
    public void run() {
        int v = page * 10000;
        for (int i = 0; i < 10000; i++) {
            synchronized (dataSet) {
                dataSet.add(v++);
            }
            count++;
            Thread.yield();
        }
    }

    public static void main(String[] args) {
        try (ExecutorService es = Executors.newFixedThreadPool(5)) {

            for (int i = 0; i < 20; i++) {
                es.execute(new Demo(i));
            }
            es.shutdown();

            try {
                boolean b = es.awaitTermination(10L, TimeUnit.SECONDS);
                System.out.println("b = " + b);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("dataSet.size() = " + dataSet.size());

        System.out.println("count = " + count);
    }
}
