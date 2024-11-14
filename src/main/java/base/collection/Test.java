package main.java.base.collection;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Test {

    public static void main(String[] args) {
        List<String> list = List.of("a", "b", "c");

        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>(list);


        copyOnWriteArrayList.add("d");
        copyOnWriteArrayList.add("e");

        System.out.println(copyOnWriteArrayList);

        Collections.synchronizedList(list);



    }

}
