package main.java.base.multithread.advance.sync.readfile;

public class ReadFileThread extends Thread{

    private ReaderFileListener readerFileListener;
    private String filePath;
    private long start;
    private long end;

    public ReadFileThread(ReaderFileListener readerFileListener, String filePath, long start, long end) {
        this.setName(this.getName()+"-ReadFileThread");
        this.readerFileListener = readerFileListener;
        this.filePath = filePath;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        ReadFile readFile = new ReadFile();
        readFile.setReaderFileLister(readerFileListener);
        readFile.setEncoding(readerFileListener.getEncode());

        try{
            readFile.readFileByLine(filePath,start,end+1);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
