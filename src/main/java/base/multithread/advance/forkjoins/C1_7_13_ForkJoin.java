package main.java.base.multithread.advance.forkjoins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class C1_7_13_ForkJoin extends RecursiveTask<Integer> {

    private static final long serialVersionUID = -492583456576876889L;

    private static final Logger log =
            LoggerFactory.getLogger(C1_7_13_ForkJoin.class);

    protected int start;
    protected int end;

    public C1_7_13_ForkJoin(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {

        int m = 2000, s = start, n = end, r = 0;
        List<ForkJoinTask<Integer>> lt = new ArrayList<ForkJoinTask<Integer>>();

        do {
            if (n - s < m * 1.5f) {
                for (int i = s; i <= n; i++) {
                    r += i;
                }
                log.info("Sum {} ~ {} = {}", s, n, r);
            } else {
                n = Math.min(s + m - 1, n);
                lt.add(new C1_7_13_ForkJoin(s, n).fork());
            }
            s = n + 1;
            n = end;

        } while (s <= end);

        for (ForkJoinTask<Integer> task : lt) {
            r += task.join();
        }

        return r;
    }

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool();
        int ss = 1, nn = 10001;
        Future<Integer> result = fjp.submit(new C1_7_13_ForkJoin(ss, nn));
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        fjp.shutdown();

        int sum = 0;
        for (int i = ss; i <= nn; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
