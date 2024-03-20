package com.example.demo.src.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements
        ConstraintValidator<PhoneNumberConstraint, String> {

    private final int PHONE_NUMBER_LENGTH = 12;
    private final Pattern PHONE_NUMBER_REGEX = Pattern.compile(String.format("^\\d{%d}$", PHONE_NUMBER_LENGTH));

    @Override
    public void initialize(PhoneNumberConstraint phoneNumber) {
    }

    @Override
    public boolean isValid(String phoneNumberField,
                           ConstraintValidatorContext cxt) {
        return phoneNumberField != null && PHONE_NUMBER_REGEX.matcher(phoneNumberField).matches();
    }

}
