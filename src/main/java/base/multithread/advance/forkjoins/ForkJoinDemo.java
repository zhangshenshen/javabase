package main.java.base.multithread.advance.forkjoins;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;


public class ForkJoinDemo {

    private final static Logger log = LoggerFactory.getLogger(ForkJoinDemo.class);

    public static void main(String[] args) {

        int n = 20;

        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            ForkJoinWorkerThread worker =
                    ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("my-Thread" + worker.getPoolIndex());
            return worker;
        };

        ForkJoinPool forkJoinPool = new ForkJoinPool(4, factory, null, false);

        FibonacciTask fibonacciTask = new FibonacciTask(n);

        Integer result = forkJoinPool.invoke(fibonacciTask);
        log.info("Fibonacci task {} result: {}", n, result);


    }


    static class FibonacciTask extends RecursiveTask<Integer> {

        final int n;

        FibonacciTask(int n) {
            this.n = n;
        }


        @Override
        protected Integer compute() {

            if (n <= 1) {
                return n;
            }
            log.info(Thread.currentThread().getName());
            FibonacciTask f1Task = new FibonacciTask(n - 1);
            f1Task.fork();
            FibonacciTask f2Task = new FibonacciTask(n - 2);

            return f2Task.compute() + f1Task.join();
        }
    }

}
