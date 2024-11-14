package main.java.base.multithread.advance.forkjoins;

import java.util.concurrent.RecursiveTask;

public class WordCountTask extends RecursiveTask<Integer> {
    private final String[] fc;
    private final int start, end;

    public WordCountTask(String[] fc, int start, int end) {
        this.fc = fc;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 1) {
            return countWords(fc[start]);
        } else {
            int middle = (start + end) / 2;
            WordCountTask left = new WordCountTask(fc, start, middle);
            left.fork();
            WordCountTask right = new WordCountTask(fc, middle, end);
            int resultRight = right.compute();
            int resultLeft = left.join();
            return resultLeft + resultRight;
        }
    }

    private int countWords(String line) {
        String[] words = line.split(" ");
        return words.length;
    }
}
