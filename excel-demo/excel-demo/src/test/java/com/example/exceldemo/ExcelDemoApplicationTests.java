package com.example.exceldemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ExcelDemoApplicationTests {

    @Test
    void contextLoads() {
        User user = new User(1, "2");
        user.id();
        user.name();
        user.name();
        System.out.printf(" id = %d name = %s ", user.id(), user.name());
    }

}
