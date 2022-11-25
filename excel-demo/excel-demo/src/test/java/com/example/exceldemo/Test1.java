package com.example.exceldemo;

import org.junit.jupiter.api.Test;

public class Test1 {

    @Test
    public void  test1(){
        User user = new User(1, "2");

        user.id();
        user.name();
        user.name();
        System.out.printf(" id = %d name = %s ", user.id(), user.name());
    }
}
