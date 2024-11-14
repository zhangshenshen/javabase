package main.java.base.multithread.advance.sync.readfile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileSplitter {


    public static void splitFile(File file, int numThreads){
        long fileSize = file.length();
        long blockSize = fileSize / numThreads;

        List<Thread> threads = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")){
            for (int i = 0; i < numThreads; i++) {
                long start = i * blockSize;
                long end;
                if(i == numThreads-1){
                    end = fileSize;
                } else {
                    end = (i + 1) * blockSize;
                }

                int finalI = i;
                Thread thread = new Thread(() -> {
                    try (RandomAccessFile outputFile = new RandomAccessFile("output" + finalI, "rw")){
                        raf.seek(start);
                        outputFile.seek(0);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        long bytesToRead = end - start;
                        while (bytesToRead > 0 && (bytesRead = raf.read(buffer,0, (int) Math.min(buffer.length,bytesToRead))) !=-1) {
                            outputFile.write(buffer,0,bytesRead);
                            bytesToRead -= bytesRead;
                        }

                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });
                thread.start();
                threads.add(thread);
            }

            for(Thread thread : threads){
                thread.join();
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        File file = new File("src/main/resources/static/test.csv");
        int numThreads = 10;
        splitFile(file, numThreads);
    }
}
