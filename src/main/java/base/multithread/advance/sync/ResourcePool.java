package main.java.base.multithread.advance.sync;

import java.util.concurrent.Semaphore;

public class ResourcePool {
    private final Semaphore semaphore;


    public ResourcePool(int limit) {
        this.semaphore = new Semaphore(limit);
    }

    public void useResource() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " get " +
                    "resource, remain resource: " + semaphore.availablePermits() + ", wait threads: " + semaphore.getQueueLength());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ": release "
                    + "the resource");
        }
    }

    public static void main(String[] args) {
        ResourcePool resourcePool = new ResourcePool(3);
        for (int i = 0; i < 10; i++) {
            new Thread(resourcePool::useResource).start();
        }
    }
}
