package com.tamingthymeleaf.application.user;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        MultipartFile avatar = getAvatar();
        if (avatar != null) {
            try {
                user.setAvatar(avatar.getBytes());
            } catch (IOException e) {
                throw new UserServiceException(e);
            }
        }
    }
    // end::update[]
}
