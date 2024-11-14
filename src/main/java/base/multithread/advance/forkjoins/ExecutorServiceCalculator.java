package main.java.base.multithread.advance.forkjoins;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceCalculator implements Calculator {

    private int parallelism;
    private ExecutorService executor;

    public ExecutorServiceCalculator() {
        parallelism = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(parallelism);
    }

    private static class SumTask implements Callable<Long> {

        private long[] numbers;
        private int from;
        private int to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }


        @Override
        public Long call() throws Exception {
            long total = 0;
            for (int i = from; i <= to; i++) {
                total += numbers[i];
            }
            return total;
        }
    }

    @Override
    public long sumUp(long[] nums) {
        List<Future<Long>> results = new ArrayList<Future<Long>>();

        int part = nums.length / parallelism;

        for (int i = 0; i < parallelism; i++) {
            int from = i * part;
            int to = (i == parallelism - 1) ? nums.length - 1 :
                    (i + 1) * part - 1;
            results.add(executor.submit(new SumTask(nums, from, to)));
        }


        long total = 0;
        for (Future<Long> future : results) {
            try {
                total += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return total;
    }
}
