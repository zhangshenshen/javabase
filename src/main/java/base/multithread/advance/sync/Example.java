package main.java.base.multithread.advance.sync;


/**
 *   use multiThread alternative print a\b\c
 *   reference  <a href="https://www.cnblogs.com/larry1024/p/17986059">...</a>
 */
public class Example {
    private static String msgh = "this is old msg";

    static String read(){
        return msgh;
    }

    static String write(){
        msgh = "this is new msg";
        return msgh;
    }



    public static void main(String[] args) {

        Thread Tread = new Thread( ()->{
//            try {
////                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println(read());
        });

        Thread Tread2 = new Thread(Example::write);

        Tread.start();
        Tread2.start();
    }
}
