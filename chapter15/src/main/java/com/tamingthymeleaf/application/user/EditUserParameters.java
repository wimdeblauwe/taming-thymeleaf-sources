package com.tamingthymeleaf.application.user;

import java.time.LocalDate;

public class EditUserParameters extends CreateUserParameters {
    private final long version;

    public EditUserParameters(long version, UserName userName, Gender gender, LocalDate birthday, Email email, PhoneNumber phoneNumber) {
        super(userName, null, gender, birthday, email, phoneNumber);
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    // tag::update[]
    public void update(User user) {
        user.setUserName(getUserName());
        user.setGender(getGender());
        user.setBirthday(getBirthday());
        user.setEmail(getEmail());
        user.setPhoneNumber(getPhoneNumber());
    }
    // end::update[]
}
