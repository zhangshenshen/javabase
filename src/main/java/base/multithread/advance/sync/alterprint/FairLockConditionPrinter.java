package main.java.base.multithread.advance.sync.alterprint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairLockConditionPrinter {


    static class ConditionPrinterEnhance{
        private final Lock lock = new ReentrantLock(true);
        private final Condition condition = lock.newCondition();
        private volatile  int count = 0;
        private final int threadNumber =10;
        public void print(int target, char content){
            lock.lock();
            try {
                for (int i = 0; i < threadNumber; i++) {
                    while (count % 3 != target){
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName() + ":" + content + ":" + count);
                    count ++;
                    condition.signal();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                lock.unlock();
            }
        }

        public void print(){
            char content = 'a';
            for (int i = 0; i < 3; i++) {
                final int k = i;
                new Thread(()->print(k, (char) (content + k))).start();
            }
        }
    }

    public static void main(String[] args) {
        new ConditionPrinterEnhance().print();
    }


    static class Test{
        private static final Lock lock = new ReentrantLock();

        private static final Condition c1 = lock.newCondition();
        private static final Condition c2 = lock.newCondition();
        private static final Condition c3 = lock.newCondition();

        private void printABC(Condition current ,Condition next){
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try{
                    System.out.println(Thread.currentThread().getName());
                    next.signal(); //唤醒下一个线程，而不是唤醒所有线程
                    current.await(); // 可以在最后一个线程执行的时候，跳过不等待，避免最后一个线程执行完后挂起
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }

        public static void main(String[] args) {
            Test test = new Test();
            new Thread(()->test.printABC(c1,c2),"a").start();
            new Thread(()->test.printABC(c2,c3),"b").start();
            new Thread(()->test.printABC(c3,c1),"c").start();
        }


    }
}
