package main.java.base.multithread.advance.sync.alterprint;

import java.util.concurrent.Semaphore;

public class SemaphorePrinter {

    static class  Printer{
        private final int n = 10;
        private final Semaphore s1 = new Semaphore(1);
        private final Semaphore s2 = new Semaphore(0);
        private final Semaphore s3 = new Semaphore(0);

        private void print(char content, Semaphore current, Semaphore next){
            for (int i = 0; i < n; i++){
                try{
                    current.acquire();
                    System.out.println(content);
                    next.release();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }

        public void print(){
            new Thread(()-> print('a',s1,s2)).start();
            new Thread(()-> print('b',s2,s3)).start();
            new Thread(()-> print('c',s3,s1)).start();
        }
    }

    public static void main(String[] args) {
        new Printer().print();

    }
}
