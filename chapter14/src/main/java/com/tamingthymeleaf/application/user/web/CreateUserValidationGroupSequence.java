package com.tamingthymeleaf.application.user.web;

import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupOne;
import com.tamingthymeleaf.application.infrastructure.validation.ValidationGroupTwo;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroupOne.class, ValidationGroupTwo.class})
public interface CreateUserValidationGroupSequence {
}
