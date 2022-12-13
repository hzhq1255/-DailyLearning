package org.example.enums;

public enum OrderEnum {

    NORMAL(OrderUser.of("normal", "0")),
    VIP(OrderUser.of("vip", "1")),

    ;
    public final OrderUser customuser;

    OrderEnum(OrderUser customuser) {
        this.customuser = customuser;
    }
}
