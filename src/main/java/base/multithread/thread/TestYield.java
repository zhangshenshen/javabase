package main.java.base.multithread.thread;

public class TestYield {

    public static void main(String[] args) {

        MyThread myThread1 = new MyThread("thread-1");
        MyThread myThread2 = new MyThread("thread-2");
        myThread1.start();
        myThread2.start();

    }

    public static class MyThread extends Thread {

        MyThread(String name) {
            super(name);
        }


        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                if (i % 2 == 0) {
                    Thread.yield();
                    System.out.println(System.currentTimeMillis()+":"+ getName() + " :" +i);
                }
            }
        }
    }
}
