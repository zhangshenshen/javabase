package main.java.base.multithread.advance.sync.alterprint;

public class Example {

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        Thread thread1 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    System.out.println("a");
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        });


        Thread thread2 = new Thread(()->{
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    System.out.println("b");
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        thread1.start();

        thread2.start();




    }
}
