package main.java.base.multithread.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Test implements Callable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Test test = new Test();
        FutureTask futureTask = new FutureTask(test);
        new Thread(futureTask).start();
        String result = (String) futureTask.get();
        System.out.println(result);
        System.out.println(Thread.currentThread().getName());
    }



    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        return "done";
    }
}
