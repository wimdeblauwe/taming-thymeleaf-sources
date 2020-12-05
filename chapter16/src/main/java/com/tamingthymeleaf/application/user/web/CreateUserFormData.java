package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupTwo;
import com.tamingthymeleaf.application.user.CreateUserParameters;
import com.tamingthymeleaf.application.user.Email;
import com.tamingthymeleaf.application.user.PhoneNumber;
import com.tamingthymeleaf.application.user.UserName;

import javax.validation.constraints.NotBlank;

@PasswordsMatch(groups = ValidationGroupTwo.class)
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

    // tag::toParameters[]
    public CreateUserParameters toParameters() {
        CreateUserParameters parameters = new CreateUserParameters(new UserName(getFirstName(), getLastName()),
                                                                   password,
                                                                   getGender(),
                                                                   getBirthday(),
                                                                   new Email(getEmail()),
                                                                   getPhoneNumber());

        if (getAvatarFile() != null
                && !getAvatarFile().isEmpty()) { //<.>
            parameters.setAvatar(getAvatarFile());
        }
        return parameters;
    }
    // end::toParameters[]
}
