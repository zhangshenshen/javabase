package main.java.base.multithread.runnable;

public class TestR implements Runnable{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new TestR());
        thread.start();
    }
}
