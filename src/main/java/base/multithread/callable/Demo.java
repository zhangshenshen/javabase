package main.java.base.multithread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> future = es.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
//                Thread.sleep(2000);

                System.out.println("task exec=======");
                return "hello world";
            }
        });

        System.out.println("main run=======");


        Thread.sleep(2000);

        try {
            String s = future.get();
            System.out.println(s);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
