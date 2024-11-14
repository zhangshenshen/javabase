package main.java.base.multithread.advance.sync.readfile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Observable;

/**
 * 基本思路如下：
 *
 * 1.计算出文件总大小
 * 2.分段处理,计算出每个线程读取文件的开始与结束位置
 * (文件大小/线程数)*N,N是指第几个线程,这样能得到每个线程在读该文件的大概起始位置
 * 使用"大概起始位置",作为读文件的开始偏移量(fileChannel.position("大概起始位置")),来读取该文件,
 * ,记录下这个换行符的位置,作为该线程的准确起 始位置.同时它也是上一个线程的结束位置.最后一个线程的结束位置也直接设置为-1
 * 3.启动线程,每个线程从开始位置读取到结束位置为止
 */

public class ReadFile extends Observable {

    private final int bufferSize = 1024;
    private final byte key = "\n".getBytes()[0];
    private long lineNum = 0;

    private String encoding = "UTF-8";

    private ReaderFileListener readerFileLister;

    public long getStartNum(File file, long position) throws Exception {
        long startNum = position;
        try (FileChannel fileChannelIn = new RandomAccessFile(file, "r").getChannel()) {
            try (fileChannelIn) {
                fileChannelIn.position(position);
                int cache = 1024;
                ByteBuffer rBuffer = ByteBuffer.allocate(cache);
                byte[] bytes = new byte[cache];
                byte[] temp = new byte[0];
                String line = "";
                while (fileChannelIn.read(rBuffer) != -1) {
                    int rSize = rBuffer.position();
                    rBuffer.rewind();
                    rBuffer.get(bytes);
                    rBuffer.clear();
                    byte[] newStrBytes = bytes;

                    if (null != temp) {
                        int tl = temp.length;
                        newStrBytes = new byte[rSize + tl];
                        System.arraycopy(temp, 0, newStrBytes, 0, tl);
                        System.arraycopy(bytes, 0, newStrBytes, tl, rSize);
                    }

                    int endIndex = indexOf(newStrBytes, 0);
                    if (endIndex != -1) {
                        return startNum + endIndex;
                    }
                    temp = subString(newStrBytes, 0, newStrBytes.length);
                    startNum += 1024;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return position;
    }


    public void readFileByLine(String fullPath, long start, long end) throws Exception {
        File fileIn = new File(fullPath);
        if(fileIn.exists()){
            FileChannel fileChannelIn = new RandomAccessFile(fileIn, "r").getChannel();
            fileChannelIn.position(start);

            try {
                ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
                byte[] bytes = new byte[bufferSize];
                byte[] tempBytes = new byte[0];
                String line = "";
                long nowCursor = start;
                while (fileChannelIn.read(readBuffer) != -1) {
                    nowCursor += bufferSize;

                    int readSize = readBuffer.position();
                    readBuffer.rewind();
                    readBuffer.get(bytes);
                    readBuffer.clear();
                    byte[] newStrBytes = bytes;
                    if (null != tempBytes) {
                        int tl = tempBytes.length;
                        newStrBytes = new byte[readSize + tl];
                        System.arraycopy(tempBytes, 0, newStrBytes, 0, tl);
                        System.arraycopy(bytes, 0, newStrBytes, tl, readSize);
                    }
                    boolean isEnd = false;
                    if (end>0 && nowCursor >= end) {
                        int l = newStrBytes.length - (int)(nowCursor -end);
                        newStrBytes = subString(newStrBytes, 0, l);
                        isEnd = true;
                    }

                    int fromIndex = 0;
                    int endIndex = 0;

                    while ((endIndex = indexOf(newStrBytes,fromIndex)) != -1) {
                        byte[] lineBytes = subString(newStrBytes, fromIndex, endIndex);
                        line = new String(lineBytes,0,lineBytes.length, encoding);
                        lineNum++;
                        readerFileLister.outLine(line.trim(),lineNum,false);
                        fromIndex = endIndex +1;
                    }
                    tempBytes = subString(newStrBytes, 0, newStrBytes.length);
                    if (isEnd){
                        break;
                    }
                }
                String lineStr = new String(tempBytes,0,tempBytes.length);
                readerFileLister.outLine(lineStr.trim(),lineNum,true);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                fileChannelIn.close();
            }

        }else {
            throw new FileNotFoundException("File not found" + fullPath);
        }

        setChanged();
        notifyObservers(start + "-" + end);
    }

    private int indexOf(byte[] src,int fromIndex){

        for(int i=fromIndex;i<src.length;i++){
            if(src[i]==key){
                return i;
            }
        }
        return -1;
    }

    private byte[] subString(byte[] src,int fromIndex,int endIndex){
        int size=endIndex-fromIndex;
        byte[] ret = new byte[size];
        System.arraycopy(src,fromIndex,ret,0,size);
        return ret;
    }


    public void setReaderFileLister(ReaderFileListener readerFileListener) {
    }

    public void setEncoding(Object encode) {
        return;
    }
}
