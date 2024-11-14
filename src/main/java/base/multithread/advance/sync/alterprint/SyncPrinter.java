package main.java.base.multithread.advance.sync.alterprint;

public class SyncPrinter {
    static class Printer{

        private final Object lock = new Object();
        private int count = 0;
        public void print(int n,int target,String content){
            for(int i=0;i<n;){
                synchronized(lock){
                    while(count%3 != target){

                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(content);
                    count++;
                    i++;
                    lock.notifyAll();
                }
            }
        }

        public void print(){
            new Thread(()->{print(10,0,"a");}).start();
            new Thread(()->{print(10,1,"b");}).start();
            new Thread(()->{print(10,2,"c");}).start();
        }
    }

    public static void main(String[] args) {
        new Printer().print();
    }
}
