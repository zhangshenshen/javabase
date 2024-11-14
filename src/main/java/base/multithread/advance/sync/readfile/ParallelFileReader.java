package main.java.base.multithread.advance.sync.readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelFileReader {
    public static void main(String[] args) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("src/main/resources/static/file1.txt");
        filePaths.add("src/main/resources/static/file2.txt");
        filePaths.add("src/main/resources/static/file3.txt");

        List<Future<List<String>>> futures;
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {

            futures = new ArrayList<>();

            // submit job
            for (String filePath : filePaths) {
                Callable<List<String>> fileReader = () -> readLinesFromFile(filePath);
                Future<List<String>> future = executorService.submit(fileReader);
                futures.add(future);
            }
        }

        //wait job down
        for (Future<List<String>> future : futures) {
            try {
                List<String> lines = future.get();
                for (String line : lines) {
                    System.out.println(line);
                }
            }catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private static List<String> readLinesFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) !=null){
                lines.add(line);
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }
}
