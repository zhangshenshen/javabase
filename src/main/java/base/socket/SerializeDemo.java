package main.java.base.socket;

import java.io.*;

public class SerializeDemo implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    static String sexes = "male";

    transient int grade = 6;

    private int height = 175;

    @Override
    public String toString() {
        return "SerializeDemo{"
                + "name='"
                + name
                + '\''
                + ", age="
                + age

                + ", sex="
                + sexes
                + ", grade="
                + grade
                + ", height="
                + height
                + '}';
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        SerializeDemo demo = new SerializeDemo();
        demo.age = 18;
        SerializeDemo.sexes = "female";

        demo.grade = 7;
        demo.height = 176;

        System.out.println("demo = " + demo);
        FileOutputStream fos = new FileOutputStream("person.txt");
        ObjectOutputStream obs = new ObjectOutputStream(fos);
        obs.writeObject(demo);

        demo.age = 19;
        SerializeDemo.sexes = "male1";

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person.txt"));

        SerializeDemo demo2 = (SerializeDemo) ois.readObject();

        System.out.println("demo2 = " + demo2);
    }
}
