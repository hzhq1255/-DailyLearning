package com.example.exceldemo;

public record User(Integer id, String name) {
    public User(){
        this(null, null);
    }
}
