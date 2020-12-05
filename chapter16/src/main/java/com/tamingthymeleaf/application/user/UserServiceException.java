package com.tamingthymeleaf.application.user;

public class UserServiceException extends RuntimeException {
    public UserServiceException(Exception e) {
        super(e);
    }
}
