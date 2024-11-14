package main.java.base.multithread.advance.sync.alterprint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnfairLockConditionPrinter {

    static class ConditionPrinter{
        private final Lock lock = new ReentrantLock();// 非公平锁
        private volatile int count = 0;
        private final int threadNumber = 10;
        private final Condition condition1 = lock.newCondition();
        private final Condition condition2 = lock.newCondition();
        private final Condition condition3 = lock.newCondition();

        public void print(int target, String content,Condition current, Condition next){

            for (int i = 0; i < threadNumber; i++) {
                lock.lock();
                try {
                    // 执行临界区代码前判断：防止锁被不满足条件的线程抢占
                    while (count%3 != target){
                        current.await();// 条件等待并释放锁
                    }
                    System.out.println(content);
                    count ++; // 注意：这不是一个原子操作
                    next.signal();// 唤醒一个等待该条件的线程
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }

        public void print(){
            new Thread(()-> print(0,"a",condition1,condition2)).start();
            new Thread(()-> print(1,"b",condition2,condition3)).start();
            new Thread(()-> print(2,"c",condition3,condition1)).start();
        }
    }

    public static void main(String[] args) {
        new ConditionPrinter().print();
    }

}
