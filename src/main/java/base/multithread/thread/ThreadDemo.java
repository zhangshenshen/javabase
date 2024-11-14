package main.java.base.multithread.thread;

public class ThreadDemo {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("t1");
            }
        }, "test1");

        Thread t2 = new Thread(() -> {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("t2");
            }
        }, "test2");

        t1.start();
        t2.start();

        System.out.println("Thread.activeCount() = " + Thread.activeCount());
        System.out.println("Thread.getAllStackTraces() = " + Thread.getAllStackTraces());
        System.out.println("Thread.currentThread().getThreadGroup() = " + Thread.currentThread().getThreadGroup());

        while (true) {
            Thread.sleep(1000);
            System.out.println("main");
        }
    }
}
