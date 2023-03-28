package com.tamingthymeleaf.application.user;

import io.github.wimdeblauwe.jpearl.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "tt_user")
public class User extends AbstractEntity<UserId> {

    @NotNull
    private UserName userName; //<.>
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender; //<.>
    @NotNull
    private LocalDate birthday; //<.>
    @NotNull
    private Email email; //<.>
    @NotNull
    private PhoneNumber phoneNumber; //<.>

    protected User() {
    }

    public User(UserId id,
                UserName userName,
                Gender gender,
                LocalDate birthday,
                Email email,
                PhoneNumber phoneNumber) {
        super(id);
        this.userName = userName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserName getUserName() {
        return userName;
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
}
