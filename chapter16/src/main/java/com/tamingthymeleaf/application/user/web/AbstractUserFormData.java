package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupOne;
import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupTwo;
import com.tamingthymeleaf.application.user.Gender;
import com.tamingthymeleaf.application.user.PhoneNumber;
import com.tamingthymeleaf.application.user.UserRole;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NotExistingUser(groups = ValidationGroupTwo.class)
public class AbstractUserFormData {
    @NotNull
    private UserRole userRole; //<.>
    @NotBlank
    @Size(min = 1, max = 200, groups = ValidationGroupOne.class)
    private String firstName;
    @NotBlank
    @Size(min = 1, max = 200, groups = ValidationGroupOne.class)
    private String lastName;
    @NotNull
    private Gender gender;
    @NotBlank
    @Email(groups = ValidationGroupOne.class)
    private String email;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    // tag::phoneNumber[]
    @NotNull
    private PhoneNumber phoneNumber;
    // end::phoneNumber[]
    private MultipartFile avatarFile;


    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}
