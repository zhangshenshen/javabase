package main.java.base.multithread.advance.sync.alterprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockPrinter {

    private static final Logger log = LoggerFactory.getLogger(ReentrantLockPrinter.class);

    static class Printer{
        private final Lock lock = new ReentrantLock();
        private int count = 0;


        public void print(int n,int target,String content){
            for(int i=0;i<n;){
                lock.lock();
                try {
                    while (count%3 == target){
                        System.out.println(content);
                        count++;
                        i++;
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }

        public void print(){
            new Thread(()->{print(10,0,"a");}).start();
            new Thread(()->{print(10,1,"b");}).start();
            new Thread(()->{print(10,2,"c");}).start();
        }

    }

    public static void main(String[] args) {

        new Printer().print();

    }
}
