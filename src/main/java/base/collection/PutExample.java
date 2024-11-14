package main.java.base.collection;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

class PutExample {
    public static void main(String[] args) throws InterruptedException {
        // 使用普通 HashMap
        HashMap<String, Integer> normalMap = new HashMap<>();
        // 使用 ConcurrentHashMap
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                normalMap.put("key" + i, i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                normalMap.put("key" + i, i + 1000);
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentMap.put("key" + i, i);
            }
        });

        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentMap.put("key" + i, i + 1000);
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Normal HashMap size: " + normalMap.size());
        System.out.println("ConcurrentHashMap size: " + concurrentMap.size());
    }
}