package main.java.base.multithread.advance.sync.readfile;


import java.util.ArrayList;
import java.util.List;

public abstract class ReaderFileListener {

    private int readColNum = 500;


    private String encode;
    private final List<String> list = new ArrayList<>();

    public void outLine(String lineStr, long lineNum, boolean over) {
        if(null != lineStr){
            list.add(lineStr);
        }

        if (!over && (lineNum % readColNum == 0)) {
            output(list);
            list.clear();
        }else if(over){
            output(list);
            list.clear();
        }

    }

    public abstract void output(List<String> list);

    public Object getEncode() {
        return encode;
    }
}
