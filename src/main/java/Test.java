package main.java;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Future<String> future = Executors.newSingleThreadExecutor().submit(() -> "hi");

        CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName()));

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(result -> result + "zss");

        CompletableFuture<Void> future2 = CompletableFuture.supplyAsync(() -> "hello")
                .thenApply(result -> result + "zss")
                .thenAccept(result -> System.out.println(result));


        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Exception");
            }
            return "Hello";
        }).exceptionally(ex -> "面试鸭");


        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Exception");
            }
            return "Hello";
        }).handle((result, ex) -> {
            if (ex != null) {
                return "Default Value";
            }
            return result;
        });

        try {
            future3.get();
            future4.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<String> future5
                = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("Computation error!");
            }
            return "hello!";
        }).handle((res, ex) -> {
            // res 代表返回的结果
            // ex 的类型为 Throwable ，代表抛出的异常
            System.out.println(ex.getMessage());
            return res != null ? res : "world!";
        });
//        assertEquals("world!", future5.get());
        System.out.println(future5.get());


        ForkJoinPool.commonPool().execute(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(10);
        PriorityBlockingQueue<String> priorityBlockingQueue = new PriorityBlockingQueue<>(10);
        DelayQueue<Delayed> delayQueue = new DelayQueue<Delayed>();
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();
        LinkedTransferQueue<String> linkedTransferQueue = new LinkedTransferQueue<>();


        LongAdder longAdder = new LongAdder();
        LongAccumulator accumulator = new LongAccumulator((x,y)-> x+y,0);

        ReentrantLock reentrantLock = new ReentrantLock();

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    }

}
