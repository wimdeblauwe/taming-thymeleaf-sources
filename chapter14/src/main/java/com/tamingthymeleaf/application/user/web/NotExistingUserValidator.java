package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.user.Email;
import com.tamingthymeleaf.application.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotExistingUserValidator implements ConstraintValidator<NotExistingUser, AbstractUserFormData> { //<.>

    private final UserService userService;

    public NotExistingUserValidator(UserService userService) {
        this.userService = userService;
    }

    public void initialize(NotExistingUser constraint) {
        // intentionally empty
    }

    public boolean isValid(AbstractUserFormData formData, ConstraintValidatorContext context) { //<.>
        if (userService.userWithEmailExists(new Email(formData.getEmail()))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{UserAlreadyExisting}")
                   .addPropertyNode("email")
                   .addConstraintViolation();

            return false;
        }

        return true;
    }
}
