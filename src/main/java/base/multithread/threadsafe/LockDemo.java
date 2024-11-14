package main.java.base.multithread.threadsafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {

    private static final Lock lock = new ReentrantLock();

    public void run(){

        lock.lock();

        try {
            System.out.println("in lock");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            lock.unlock();
        }


    }
}
