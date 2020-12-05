package com.tamingthymeleaf.application.user;

import io.github.wimdeblauwe.jpearl.AbstractVersionedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

// tag::class-until-password[]
@Entity
@Table(name = "tt_user")
public class User extends AbstractVersionedEntity<UserId> {

    @ElementCollection(targetClass = UserRole.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles")
    @Column(name = "role")
    private Set<UserRole> roles;

    @NotNull
    private String password;
    // end::class-until-password[]
    @NotNull
    private UserName userName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private Email email;
    @NotNull
    private PhoneNumber phoneNumber;
    private byte[] avatar;

    protected User() {
    }

    // tag::constructors[]
    private User(UserId id,
                 Set<UserRole> roles,
                 UserName userName,
                 String password,
                 Gender gender,
                 LocalDate birthday,
                 Email email,
                 PhoneNumber phoneNumber) {
        super(id);
        this.roles = roles;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static User createUser(UserId id,
                                  UserName userName,
                                  String encodedPassword,
                                  Gender gender,
                                  LocalDate birthday,
                                  Email email,
                                  PhoneNumber phoneNumber) {
        return new User(id, Set.of(UserRole.USER), userName, encodedPassword, gender, birthday, email, phoneNumber);
    }

    public static User createAdministrator(UserId id,
                                           UserName userName,
                                           String encodedPassword,
                                           Gender gender,
                                           LocalDate birthday,
                                           Email email,
                                           PhoneNumber phoneNumber) {
        return new User(id, Set.of(UserRole.USER, UserRole.ADMIN), userName, encodedPassword, gender, birthday, email, phoneNumber);
    }
    // end::constructors[]

    public Set<UserRole> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
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

    public void setUserName(UserName userName) {
        this.userName = userName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // tag::avatar-methods[]

    /**
     * The avatar image of the driver. Null if no avatar has been set.
     *
     * @return the image bytes
     */
    public byte[] getAvatar() {
        return avatar;
    }

    /**
     * Set the avatar image of the driver.
     *
     * @param avatar the image bytes
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
    // end::avatar-methods[]
}
