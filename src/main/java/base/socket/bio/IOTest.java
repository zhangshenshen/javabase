package main.java.base.socket.bio;

import main.java.base.entity.User;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class IOTest {


    public static void main(String[] args) throws IOException {

        String path = "D://develop/test.txt";

        User user = new User();
        user.setAge(10);
        user.setName("zss");
        user.setSex("male");


        ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(path));

        FileWriter fileWriter = new FileWriter(path);

//        oos.writeObject(user);
        System.out.println(user.toString());
//        oos.writeChars(user.toString());

//        oos.close();

        fileWriter.write(user.toString());

        fileWriter.close();



    }
}
