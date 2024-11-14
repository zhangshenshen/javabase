package main.java.base.multithread.advance.sync.readfile;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ComputeAverage {


    int total = 1000000;
    int threadNum = 10;

    File file = new File("src/main/resources/static/test.csv");
    File avgFile = new File("src/main/resources/static/avg.csv");
    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        new ComputeAverage().computeAverage();
//        new ComputeAverage().computeAverageWithMultiThread();

        long endTime = System.currentTimeMillis();

        System.out.println("total time: " + (endTime - startTime));

    }



    void computeAverage() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file); FileOutputStream fileOutputStream = new FileOutputStream(avgFile)) {
            Scanner scanner = new Scanner(fileInputStream);

            double sum = 0;
            int count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");
                double average = Arrays.stream(split).mapToDouble(Double::parseDouble).average().getAsDouble();
                fileOutputStream.write(Double.toString(average).getBytes());
                fileOutputStream.write("\n".getBytes());

                System.out.println(average);
                sum += average;
                count++;
            }
            System.out.println("count line :" +count);
            System.out.println("sum:" + sum);
            System.out.println("avg:" + sum/count);
        }


    }


    class MyRunnable implements Runnable {
        private final int finalI;
        MyRunnable(int finalI) {
            this.finalI = finalI;
        }
        @Override
        public void run() {
            try (RandomAccessFile reader = new RandomAccessFile(file, "r");
                 RandomAccessFile writer = new RandomAccessFile(avgFile, "rw")) {
                long start = (long) finalI * total / threadNum;
                long startTime = System.currentTimeMillis();
                try {
                        reader.seek(start);
                        writer.seek(start);
                        int count = 0;
                        double sum = 0;
                        while (count<100000) {
                            String line = reader.readLine();
                            String[] split = line.split(",");
                            double average = Arrays.stream(split).mapToDouble(Double::parseDouble).average().getAsDouble();
                            System.out.println("average = " + average);
                            writer.write(Double.toString(average).getBytes());
                            writer.write("\n".getBytes());
                            count++;
                            sum += average;
                        }

                        System.out.println("===================start : " + start);
                        System.out.println("count = " + count);
                        System.out.println("sum = " + sum);
                        System.out.println("avg = " + sum/count);

                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("total time: " + (endTime - startTime));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    void computeAverageWithMultiThread(){
        for (int i = 0; i < threadNum; i++) {
            new Thread(new MyRunnable(i)).start();
        }
    }
    }
