package main.java.base.multithread.thread;

public class TestInterrupt {

    public static void main(String[] args) {
        Thread thread = new Thread(()->{

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"thread-1");
        thread.start();
        thread.interrupt();
    }
}
