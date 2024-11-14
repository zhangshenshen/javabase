package main.java.base.stream;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.StampedLock;

public class StreamTest {

    public static void main(String[] args) {
        ArrayList arrayList = new ArrayList();
        Semaphore semaphore = new Semaphore(1);

        CyclicBarrier barrier = new CyclicBarrier(2);

        CountDownLatch countDownLatch = new CountDownLatch(2);

        StampedLock stampedLock = new StampedLock();







    }


}
