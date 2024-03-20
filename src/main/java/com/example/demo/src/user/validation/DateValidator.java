package com.example.demo.src.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements
        ConstraintValidator<DateConstraint, String> {

    private boolean nullable;
    @Override
    public void initialize(DateConstraint date) {
        this.nullable = date.nullable();
    }

    @Override
    public boolean isValid(String dateField,
                           ConstraintValidatorContext cxt) {
        if (!nullable && dateField == null) {
            return false;
        }

        try {
            LocalDate.parse(dateField);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
