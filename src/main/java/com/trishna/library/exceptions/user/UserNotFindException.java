package com.trishna.library.exceptions.user;

public class UserNotFindException extends RuntimeException{
    public UserNotFindException(String message){
        super(message);
    }
}
