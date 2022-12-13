package org.example.struct;

public record User(Integer userId, String username) {

    public static User of(){
        return new User(null, null);
    }


    public static User of(Integer userId, String username){
        return new User(userId, username);
    }



}
