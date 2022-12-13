package org.example;

import org.example.enums.OrderEnum;
import org.example.struct.User;
import org.junit.jupiter.api.Test;

public class RecordUserTest {

    @Test
    public void TestGetUser(){
        User user = new User(1,"aaa");
        System.out.printf("userId=%d, username=%s", user.userId(), user.username());
    }

    @Test
    public void TestCreateUser(){
        var user = User.of(2, "bbb");
        System.out.printf("userId=%d, username=%s", user.userId(), user.username());
    }

    @Test
    public void TestOrderUser(){
        String type =  OrderEnum.VIP.customuser.type();
        String strategy = OrderEnum.VIP.customuser.strategy();
        System.out.printf("type=%s, strategy=%s", type, strategy);
    }

}
