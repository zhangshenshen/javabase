package main.java.base.multithread.advance.sync.readfile;

import java.util.List;

public class ProcessDataListeners extends ReaderFileListener{
    @Override
    public void output(List<String> list) {
        for (String line : list) {
            System.out.println(line);
        }
    }
}
