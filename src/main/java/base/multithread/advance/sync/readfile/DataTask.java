package main.java.base.multithread.advance.sync.readfile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/* 将写入工作交给一个线程 */
/* 加工数据的Task。生产者 */
class DataTask implements Runnable{
    BlockingQueue<String> data_put;
    private int start;
    private int end;

    public DataTask(BlockingQueue<String> data_put, int start, int end){
        this.data_put = data_put;
        this.start = start;
        this.end = end;
    }
    @Override
    public void run(){
            /*System.out.println(String.format("%s-%s:%d-%d开始",
                    System.currentTimeMillis(),
                    Thread.currentThread().getName(),
                    this.start,this.end));*/
        for(int i=start;i<=end;i++){

            try {
                /* 加工数据需要时间，随机 */
                Thread.sleep(10+new Random().nextInt(20));
                String s = String.format("%s-%s:%d-%d[%d]\n",
                        System.currentTimeMillis(),
                        Thread.currentThread().getName(),
                        this.start,this.end,i);
                data_put.put(i+"\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;

            }
        }
        System.out.println(String.format("%s-%s:%d-%d结束",
                System.currentTimeMillis(),
                Thread.currentThread().getName(),
                this.start,this.end));

    }

    static class WriteTask implements Runnable{
        private BlockingQueue<String> data_in = new ArrayBlockingQueue<>(10);
        private byte[] buffer = new byte[1024];
        private int th = (int)(1024*0.8);
        int length=0;
        private String fileName;

        public WriteTask(String fileName){
            this.fileName = fileName;
            try {
                /* 清空要写入数据的文件 */
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public BlockingQueue<String> get_Queue(){
            return data_in;
        }
        private void  write(){
            if(length==0) return;
            try {
                //System.out.println(length);
                //System.out.println(new String(buffer));
                System.out.println("开始写入……");
                FileOutputStream fileOutputStream = new FileOutputStream(fileName,true);
                fileOutputStream.write(buffer,0,length);
                fileOutputStream.close();
                System.out.println(length+"写入完成。");
                length = 0;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        private void close(){
            //System.out.println(new String(buffer));
            this.write();
            // System.out.println(length);
            //data_in = null;
        }
        @Override
        public void run(){
            while (true){
                try {
                    byte[] tmp= data_in.take().getBytes();
                    System.arraycopy(tmp,0,buffer,length,tmp.length);
                    length = length+tmp.length;

                    if(length>=th){
                        this.write();
                    }
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    break;

                }
            }
        }
    }



    /* 缓存提交的数据，并写入 */
    public void test3(){
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        String fileName = "b.csv";

        WriteTask writeTask = new WriteTask(fileName);
        pool.execute(writeTask);

        int num = 20;
        int writenum = 100;

        for(int i=0;i<num;i++){
            //System.out.println(i*writenum+"---"+((i+1)*writenum-1));
            pool.execute(new DataTask(writeTask.get_Queue(),i*writenum,((i+1)*writenum-1)));
        }
        pool.shutdown();

        while (true){
            try {
                pool.awaitTermination(500, TimeUnit.MILLISECONDS);
                if(pool.getActiveCount()==1){
                    writeTask.close();
                    Thread.sleep(10);
                    pool.shutdownNow();
                }
                if(pool.getActiveCount()==0){
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

