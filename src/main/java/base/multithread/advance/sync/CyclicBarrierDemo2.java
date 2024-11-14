package main.java.base.multithread.advance.sync;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo2 {
    static class PerTaskThread implements Runnable {
        private final String task;
        private final CyclicBarrier cyclicBarrier;

        public PerTaskThread(String task, CyclicBarrier cyclicBarrier) {
            this.task = task;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            for (int i = 1; i < 4; i++) {
                try {
                    Random random = new Random();
                    Thread.sleep(random.nextInt(1000));
                    System.out.printf("关卡%d的任务%s完成%n",i,task);
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3,()->{
            System.out.println("====本关卡所有前置任务完成，开始本关卡==========");
        });

        new Thread(new PerTaskThread("加载地图",cyclicBarrier)).start();
        new Thread(new PerTaskThread("加载音乐",cyclicBarrier)).start();
        new Thread(new PerTaskThread("加载任务模型",cyclicBarrier)).start();

    }
}
