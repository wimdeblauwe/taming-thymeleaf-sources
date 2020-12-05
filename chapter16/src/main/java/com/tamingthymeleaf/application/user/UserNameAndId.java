package com.tamingthymeleaf.application.user;

public class UserNameAndId {
    private final UserId id;
    private final UserName userName;

    public UserNameAndId(UserId id, UserName userName) {
        this.id = id;
        this.userName = userName;
    }

    public UserId getId() {
        return id;
    }

    public UserName getUserName() {
        return userName;
    }
}

