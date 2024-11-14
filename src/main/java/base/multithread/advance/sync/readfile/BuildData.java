package main.java.base.multithread.advance.sync.readfile;

import java.io.File;
import java.io.FileInputStream;

public class BuildData {
    public static void main(String[] args) {
        File file = new File("src/main/resources/static/avg.csv");

        FileInputStream fis = null;

        try {
            ReadFile readFile = new ReadFile();
            fis = new FileInputStream(file);
            int available = fis.available();
            int maxThreadNum = 50;

            int i = available / maxThreadNum;
            for (int j = 0;j < maxThreadNum;j++) {

                long startNum = j==0? 0:readFile.getStartNum(file,i*j);
                long endNum = j+1 < maxThreadNum? readFile.getStartNum(file,i*(j+1)):-2;
                ProcessDataListeners processDataListeners = new ProcessDataListeners();
                new ReadFileThread(processDataListeners,file.getPath(),startNum,endNum).start();
            }

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
