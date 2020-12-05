package com.tamingthymeleaf.application.user;

import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.regex.Pattern;

// tag::class[]
public class PhoneNumber {
    private static final Pattern VALIDATION_PATTERN = Pattern.compile("[0-9.\\-() x/+]+");
    private String phoneNumber;

    protected PhoneNumber() {
    }

    public PhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber, "phoneNumber cannot be blank");
        Assert.isTrue(VALIDATION_PATTERN.asPredicate().test(phoneNumber), "phoneNumber does not have proper format");
        this.phoneNumber = phoneNumber;
    }
    // end::class[]

    public String asString() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("phoneNumber", phoneNumber)
                          .toString();
    }
}
