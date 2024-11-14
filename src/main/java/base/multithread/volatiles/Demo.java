package main.java.base.multithread.volatiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Demo implements Runnable{
//    public static volatile boolean flag = true;
    public static boolean flag = true;
    public static int value;


    @Override
    public void run() {
//        while(flag && !Thread.interrupted()){
        while (flag){
            value++;
            value--;

            try {
                Thread.sleep(0L);
            } catch (InterruptedException e) {
                return;
            }
//            System.out.println(value);
        }
        System.out.println("flag = " + flag);
    }

    public static void main(String[] args){

        long l;
        ExecutorService es = Executors.newFixedThreadPool(5);
        l = System.currentTimeMillis();
        es.execute(new Demo());
        es.execute(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            Demo.flag = false;
            System.out.println("set flag = " + Demo.flag);
        });

        es.shutdown();
        try {
            System.out.println("es.awaitTermination(4L, TimeUnit.SECONDS) = " + es.awaitTermination(4L,
                    TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(System.currentTimeMillis() - l);
    }
}
