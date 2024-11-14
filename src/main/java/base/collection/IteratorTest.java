package main.java.base.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("a","b","c");


        Iterator it = list.iterator();

        while (it.hasNext()){
            System.out.println(it.next());
        }

    }
}
