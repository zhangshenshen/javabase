package main.java.base.multithread.advance.sync.readfile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RWrite implements Runnable{
    public int pos;
    public String text;
    public String fileName;

    public RWrite(int pos,String text,String fileName){
        this.pos = pos;
        this.text = text;
        this.fileName = fileName;

    }
    @Override
    public void run(){
        try {
            RandomAccessFile rw = new RandomAccessFile(this.fileName,"rw");
            rw.seek(pos);
            rw.writeBytes(text);
            rw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void test(){
        ExecutorService pool = Executors.newFixedThreadPool(10);
        int linwsize = 20;//必须预留足够的一行的空间，否则会导致覆盖，但是预留空间过大，也会出现空的字符
        int lines = 50;//需要写入100行

        String fileName = "a.csv";

        RandomAccessFile rw = null;
        try {
            rw = new RandomAccessFile(fileName,"rw");
            rw.setLength(0);//清空文件
            /*写入标题栏*/
            rw.writeBytes("index,text\n");
            rw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        for(int i=1;i<=lines;i++){
            pool.execute(new RWrite(i*linwsize,i+", text"+i+"\n",fileName));
        }
        pool.shutdown();
    }
}
