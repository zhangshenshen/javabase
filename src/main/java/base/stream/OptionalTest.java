package main.java.base.stream;

import main.java.base.entity.User;

import java.util.NoSuchElementException;
import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {
//        Optional<User> user = Optional.ofNullable(null);

//        if(user.isPresent()){
//            System.out.println("user exist");
//        }else {
//            System.out.println("user not exist");
//        }

        User user = getUser();


        Optional.ofNullable(getUser()).map(a -> a.getAge()).map(p -> p.byteValue()).orElseThrow(NoSuchElementException::new);


    }


    public static User getUser() {
        return new User();
    }


}