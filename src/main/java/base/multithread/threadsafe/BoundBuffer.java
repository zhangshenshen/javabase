package main.java.base.multithread.threadsafe;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundBuffer<T> {
    private final LinkedList<T> buffer;
    private final int capacity;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    public BoundBuffer(int capacity) {
        this.buffer = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }


    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() >= capacity) {
                System.out.println("wait the pool to be not full");
                notFull.await();
            }
            buffer.add(t);
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                System.out.println("wait the pool to be not empty");
                notEmpty.await();
            }
            T t= buffer.removeFirst();
            notFull.signal();
            return t;
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(30);
        BoundBuffer<Integer> buffer = new BoundBuffer<Integer>(10);
        for (int i = 0; i < 2; i++) {
            executor.execute(()->{
                int count =0;
                while (true){
                    try {
                        System.out.println("[" + Thread.currentThread().getName() + "] : put :" + count);
                        buffer.put(count++);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }

        for (int i = 0; i < 2; i++) {
            executor.execute(()->{
                while (true){

                    try {
                        System.out.println("[" + Thread.currentThread().getName() + "] : take :" + buffer.get());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        }


    }
}
