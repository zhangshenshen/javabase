package main.java.base.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        List<List<String>> lists = Arrays.asList(
                Arrays.asList("apple", "banana"),
                Arrays.asList("orange", "grape")
        );
        List<String> flattenedList = lists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        lists.forEach(System.out::println);
        flattenedList.forEach(System.out::println);
    }
}
