package main.java.base.multithread.advance.sync.readfile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SplitFileOptimize {
    private static  final int THREAD_NUM = 5;
    private static void splitChunks(){
        File file = new File("src/main/resources/static/test.csv");
        long total = file.length();
        System.out.println("total = " + total);
        long chunkSize = total<THREAD_NUM?total: total/THREAD_NUM;
        long start = 0;
        Map<Long,Long> chunkMap = new HashMap<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            chunkSize = padding(start, chunkSize, file);
            handlerReportTreeBaseData(start,chunkSize);
            chunkMap.put(start,chunkSize);
            start = start + chunkSize;
        }
        CompletableFuture.allOf(chunkMap.entrySet().stream().map(entry-> CompletableFuture.runAsync(()->handlerReportTreeBaseData(entry.getKey(),entry.getValue())))
                .toArray(CompletableFuture[]::new))
                .exceptionally(throwable -> {
                    System.out.println(throwable.getMessage());
                    return null;
                })
                .join();

        System.out.println("total = " + total);
    }


    private static long padding(long start, long chunkSize, File file) {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r")){
            randomAccessFile.seek(start+chunkSize);
            boolean eol = false;
            // 判断当前的位置是否需要填充，如果当前没有数据或者是行尾，则不需要填充
            switch (randomAccessFile.read()){
                case -1:
                case '\n', '\r':
                    eol = true;
                    break;
                default:
                    break;
            }
            //如果符合填充条件，对其进行填充，首先是读取一行数据，然后计算出这行数据的长度，燃后将这行数据的长度加上前面读取的一字节，然后将这些长度加到chunksize上
            if(!eol){
                String readLine = randomAccessFile.readLine();
                chunkSize +=readLine.getBytes().length;
                chunkSize +=1;//加上前面读取的一字节
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chunkSize;
    }

    private static void handlerReportTreeBaseData(long start, long chunkSize){
        try(RandomAccessFile randomAccessFile = new RandomAccessFile("src/main/resources/static/test.csv","r")){
            randomAccessFile.seek(start);
            long currentCount = 0L;
            String line;
            while (currentCount < chunkSize && (line = randomAccessFile.readLine()) !=null){
                if(!line.isEmpty()){
                    currentCount += line.getBytes().length + System.lineSeparator().length();
                    System.out.println(line);
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SplitFileOptimize.splitChunks();
    }

}
