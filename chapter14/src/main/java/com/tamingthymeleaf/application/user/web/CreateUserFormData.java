package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupTwo;
import com.tamingthymeleaf.application.user.CreateUserParameters;
import com.tamingthymeleaf.application.user.PhoneNumber;
import com.tamingthymeleaf.application.user.UserName;

import jakarta.validation.constraints.NotBlank;

@PasswordsMatch(groups = ValidationGroupTwo.class) //<.>
public class CreateUserFormData extends AbstractUserFormData {
    @NotBlank
    private String password;
    @NotBlank
    private String passwordRepeated;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public CreateUserParameters toParameters() {
        return new CreateUserParameters(new UserName(getFirstName(), getLastName()),
                                        password,
                                        getGender(),
                                        getBirthday(),
                                        new com.tamingthymeleaf.application.user.Email(getEmail()),
                                        new PhoneNumber(getPhoneNumber()));
    }
}
