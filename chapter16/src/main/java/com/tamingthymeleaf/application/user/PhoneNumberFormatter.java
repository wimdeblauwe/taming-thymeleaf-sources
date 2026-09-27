package com.tamingthymeleaf.application.user;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class PhoneNumberFormatter implements Formatter<PhoneNumber> { //<.>
    @Override
    public PhoneNumber parse(String text, Locale locale) throws ParseException {
        return new PhoneNumber(text); //<.>
    }

    @Override
    public String print(PhoneNumber object, Locale locale) {
        return object.asString(); //<.>
    }
}
