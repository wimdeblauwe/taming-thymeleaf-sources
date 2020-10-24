package com.tamingthymeleaf.application.user;

import java.time.LocalDate;
import java.util.UUID;

public class Users {
    public static User createUser() {
        return createUser(new UserName("Henry", "Cross"));
    }

    public static User createUser(UserName userName) {
        return User.createUser(new UserId(UUID.randomUUID()),
                               userName,
                               "fake-encoded-password",
                               Gender.FEMALE,
                               LocalDate.parse("2001-06-19"),
                               new Email(String.format("%s.%s@gmail.com", userName.getFirstName().toLowerCase(), userName.getLastName().toLowerCase())),
                               new PhoneNumber("+555 123 456"));
    }
}
