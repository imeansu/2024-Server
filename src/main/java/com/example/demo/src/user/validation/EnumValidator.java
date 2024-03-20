package com.example.demo.src.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements
        ConstraintValidator<EnumConstraint, String> {
    private List<String> acceptedValues;
    private boolean nullable;
    @Override
    public void initialize(EnumConstraint enumConstraint) {
        acceptedValues = Stream.of(enumConstraint.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
        this.nullable = enumConstraint.nullable();
    }

    @Override
    public boolean isValid(String enumField,
                           ConstraintValidatorContext cxt) {
        if (!nullable && enumField == null) {
            return false;
        }

        return acceptedValues.contains(enumField);
    }
}
