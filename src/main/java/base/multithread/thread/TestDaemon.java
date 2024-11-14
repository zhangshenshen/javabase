package main.java.base.multithread.thread;

import java.util.ArrayList;
import java.util.List;

public class TestDaemon {

    private static volatile boolean restFlag = false;
    private static final int threshold = 5;

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();

        Thread worker = new Thread(()->{
            while(!restFlag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!restFlag){
                    list.add(1);
                    System.out.println("worker is working");
                }
            }
            System.out.println("worker rest");
        },"work-thread");

        Thread employers = new Thread(()->{

            while(!restFlag){
                if(list.size()>=threshold){
                    restFlag = true;
                    System.out.println("employers exit");
                }
            }

        });

        employers.setDaemon(true);
        worker.start();
        employers.start();


    }
}
