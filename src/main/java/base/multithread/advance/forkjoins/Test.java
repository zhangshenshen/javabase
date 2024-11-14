package main.java.base.multithread.advance.forkjoins;

public class Test {
    public static void main(String[] args) {
        int count = 100000000;

        final long[] numbers = new long[count+1];
        for (int i = 1; i <= count; i++) {
            numbers[i] = i;
        }


        long start = System.currentTimeMillis();
        Calculator calculator1 = new ExecutorServiceCalculator();
        System.out.println("calculator1.sumUp(numbers) = " + calculator1.sumUp(numbers));
        long end = System.currentTimeMillis();

        System.out.println("end - start = " + (end - start));


        long start1 = System.currentTimeMillis();
        Calculator calculator2 = new ForkJoinCalculator();
        System.out.println("calculator2.sumUp(numbers) = " + calculator2.sumUp(numbers));

        long end1 = System.currentTimeMillis();

        System.out.println("end1 - start1 = " + (end1 - start1));

    }
}
