package com.example.demo.src.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class LoginIdValidator implements
        ConstraintValidator<LoginIdConstraint, String> {
    private final Pattern LOGIN_ID_REGEX = Pattern.compile("^[a-zA-Z0-9_.]+$");

    @Override
    public void initialize(LoginIdConstraint loginId) {
    }

    @Override
    public boolean isValid(String loginIdField,
                           ConstraintValidatorContext cxt) {
        return loginIdField != null && LOGIN_ID_REGEX.matcher(loginIdField).matches();
    }

}
