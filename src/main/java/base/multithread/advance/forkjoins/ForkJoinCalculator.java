package main.java.base.multithread.advance.forkjoins;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinCalculator implements Calculator {
    private ForkJoinPool pool;


    private static class SumTask extends RecursiveTask<Long> {

        private long[] numbers;
        private int from, to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if (to - from < 6) {
                long total = 0;
                for (int i = from; i <= to; i++) {
                    total += numbers[i];
                }
                return total;
            }else {
                int mid = (from + to) / 2;
                SumTask left = new SumTask(numbers, from, mid);
                SumTask right = new SumTask(numbers, mid + 1, to);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }
    }

    public ForkJoinCalculator() {
        pool = new ForkJoinPool();
    }


    @Override
    public long sumUp(long[] nums) {
        Long result = pool.invoke(new SumTask(nums, 0, nums.length - 1));
        pool.shutdown();
        return result;
    }
}
