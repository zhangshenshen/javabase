package main.java.base.multithread.thread;

public class BlockThread extends Thread{

    private final String name;
    private final Object lock;

    BlockThread(String name,Object lock){
        this.name=name;
        this.lock=lock;
    }
    @Override
    public void run(){

        System.out.println("Thread"+name+"state"+ Thread.currentThread().getState());
        synchronized (lock){
            System.out.println("Thread "+name+" hold the lock");
            try {
                System.out.println("Thread "+name+" State is "+Thread.currentThread().getState());

                Thread.sleep(1000L*10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread " + name + " release the lock");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Object lock=new Object();
        BlockThread t1 = new BlockThread("t1",lock);
        BlockThread t2 = new BlockThread("t2",lock);

        t1.start();
        t2.start();
        Thread.sleep(1000L);
        System.out.println(t1.name+":"+t1.getState());
        System.out.println(t2.name+":"+t2.getState());
    }
}
