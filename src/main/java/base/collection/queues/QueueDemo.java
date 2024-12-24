package main.java.base.collection.queues;

import java.util.Comparator;
import java.util.PriorityQueue;

public class QueueDemo {
    public static void main(String[] args) {
        PriorityQueue<User> queue = new PriorityQueue<>(Comparator.comparing(User::getScore));


        queue.add(new User("zss1",7));
        queue.add(new User("zss2",5));
        queue.add(new User("zss3",4));
        queue.add(new User("zss4",6));
        queue.add(new User("zss5",1));
        queue.add(new User("zss6",3));
        queue.add(new User("zss7",10));
        queue.add(new User("zss8",6));

        while (!queue.isEmpty()){
            System.out.println(queue.poll());
        }
    }
    static class User{
        private String name;
        private int score;

        User(String name,int score){
            this.name = name;
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}
