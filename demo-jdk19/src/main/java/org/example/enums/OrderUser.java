package org.example.enums;

public record OrderUser(String type, String strategy) {
    public static OrderUser of(String type, String strategy){
        return new OrderUser(type, strategy);
    }

    public static OrderUser of(){
        return new OrderUser(null, null);
    }
}
