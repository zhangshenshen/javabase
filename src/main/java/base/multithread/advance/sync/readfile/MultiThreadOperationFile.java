package main.java.base.multithread.advance.sync.readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadOperationFile {

    static class ReadSpecificLinesThreaded{
        public static void main(String[] args) {
//            String file_path = "D:\\develop\\workspace\\SpringCloudDemo\\src\\main\\resources\\static\\test.csv";
            String file_path = "src/main/resources/static/avg.csv";

            List<String> specificLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file_path))){
                String line = null;
                while ((line = reader.readLine()) != null){
                    if(line.contains("#")){
                        specificLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {

                for (String line : specificLines) {
                    executorService.execute(new ProcessLineTask(line));
                }

                executorService.shutdown();
            }
        }

        static class ProcessLineTask implements Runnable{

            private String line;

            public ProcessLineTask(String line){
                this.line = line;
            }

            @Override
            public void run() {
                // particular logic

                System.out.println(Thread.currentThread().getName() + ": " + line);
            }
        }


    }



}
