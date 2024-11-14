package main.java.base.multithread.thread;

public class TestJoin {

    public static void main(String[] args) throws InterruptedException {
        Mythread myThread1 = new Mythread("thread-1");
        Mythread myThread2 = new Mythread("thread-2");
        myThread1.start();
        myThread1.join();
        myThread2.start();

    }


    private static class Mythread extends Thread{


        public Mythread(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                System.out.println(getName());
            }
        }
    }
}
