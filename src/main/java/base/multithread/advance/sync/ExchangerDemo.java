package main.java.base.multithread.advance.sync;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1 = "data1";
                    System.out.println(Thread.currentThread().getName() +
                            "正在把 " + data1 + "交换出去");
                    Thread.sleep(1000);

                    String data2 = exchanger.exchange(data1);
                    System.out.println(Thread.currentThread().getName() +
                            "交换到了" + data2);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1 = "data2";
                    System.out.println(Thread.currentThread().getName() +
                            "正在把" + data1 + "交换出去");
                    Thread.sleep(2000);

                    String data2 = exchanger.exchange(data1);
                    System.out.println(Thread.currentThread().getName() +
                            "交换到了" + data2);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "thread2").start();
    }
}
