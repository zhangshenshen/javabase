package main.java.base.multithread.advance.sync.readfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* 多线程复制文件 */
/* 多线程写入文件指定位置 */
/*
https://www.cnblogs.com/liwxmyself/p/15645080.html
 */
class RCopy implements Runnable{
    public int pos;
    public int len;
    public String readFile;
    public String writeFlie;

    public RCopy(String readFile,String writeFlie,int pos,int len){
        this.pos = pos;
        this.len = len;
        this.readFile = readFile;
        this.writeFlie = writeFlie;

    }
    @Override
    public void run(){
        byte[] bytes = new byte[len];
        try {
            RandomAccessFile rr = new RandomAccessFile(this.readFile,"r");
            RandomAccessFile rw = new RandomAccessFile(this.writeFlie,"rw");
            rr.seek(pos);
            rw.seek(pos);
            rr.read(bytes);
            rw.write(bytes);
            rr.close();
            rw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test2(){
        ExecutorService pool = Executors.newFixedThreadPool(10);


        String readFile = "b.csv";
        String writeFlie = "a.csv";

        long totalLen = 0;
        int len = 1024; /* 每个task写入的大小 */

        try {
            //读取需要复制文件的大小
            RandomAccessFile file = new RandomAccessFile(readFile,"r");
            totalLen = file.length();
            System.out.println("length:"+totalLen);
            //清空需要写入的文件
            file = new RandomAccessFile(writeFlie,"rw");
            file.setLength(0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int tasknum = 11;


        for(int i=0;i<totalLen;i = i+len){
            int alen = len;
            if(i+len>totalLen) alen = (int)totalLen-i;
            //System.out.println(i+":"+alen);
            pool.execute(new RCopy(readFile,writeFlie,i,alen));
        }
        pool.shutdown();

    }
}
