package com.tamingthymeleaf.application.user;

import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class CreateUserParameters {
    private final UserName userName;
    private final @Nullable String password;
    private final Gender gender;
    private final LocalDate birthday;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private @Nullable MultipartFile avatar;

    public CreateUserParameters(UserName userName,
                                @Nullable String password,
                                Gender gender,
                                LocalDate birthday,
                                Email email,
                                PhoneNumber phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserName getUserName() {
        return userName;
    }

    public @Nullable String getPassword() {
        return password;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public @Nullable MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable MultipartFile avatar) {
        this.avatar = avatar;
    }
}
