package main.java.base.multithread.thread;

public class WaitingThread extends Thread{

    private final Object lock;
    WaitingThread(String name, Object lock){
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName()+" try to wait");
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();
        WaitingThread thread = new WaitingThread("Thread1", lock);
        thread.start();
        Thread.sleep(1000L);
        System.out.println("Thread T State is " + thread.getState());
        System.out.println("Thread "+Thread.currentThread().getName()+" State is " + Thread.currentThread().getState());



        System.out.println("Thread T State is " + thread.getState());
        System.out.println("Thread "+Thread.currentThread().getName()+" State is " + Thread.currentThread().getState());
    }
}
