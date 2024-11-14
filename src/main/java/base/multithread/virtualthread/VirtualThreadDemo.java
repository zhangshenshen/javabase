package main.java.base.multithread.virtualthread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class VirtualThreadDemo {
    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
//    Thread virtualThread =
//        Thread.ofVirtual().start(() -> System.out.println("This is a virtual thread!"));
//    virtualThread.join(); // 等待虚拟线程结束
//
//    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
//    executorService.submit(() -> System.out.println("This is a virtual thread!"));
//    executorService.shutdown();
//
//    ExecutorService executorService1 =
//        new ThreadPoolExecutor(
//            10,
//            1,
//            0L,
//            TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<>(),
////            (runnable) -> Thread.startVirtualThread(runnable),
//                Thread.ofVirtual().factory(),
//            new ThreadPoolExecutor.AbortPolicy());
//
//    executorService1.submit(() -> System.out.println("This is a virtual thread!"));
//    executorService1.shutdown();

        String path = System.getProperty("user.dir");
        String filename = path + "//resources//test.txt";
//        try (FileWriter fileWriter = new FileWriter(filename)) {
//            IntStream.range(0, 1000).forEach(i -> {
//                try {
//                    fileWriter.write("line-" + i + "\n");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }


        ExecutorService worker;
        AtomicInteger count;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            worker = new ThreadPoolExecutor(2, 10, 0, TimeUnit.SECONDS, new SynchronousQueue<>());
            String line;
            count = new AtomicInteger(0);
            while ((line = bufferedReader.readLine()) != null) {
                String finalLine = line;
                Future<Integer> future = worker.submit(() -> countLines(finalLine));
                count.addAndGet(future.get());
            }
        }
        worker.shutdown();
        System.out.println("总数为：" +count.get());
    }

    public static int countLines(String line) {
        System.out.println(Thread.currentThread().getName());
        return Optional.ofNullable(line).map(String::length).orElse(0);
    }
}
