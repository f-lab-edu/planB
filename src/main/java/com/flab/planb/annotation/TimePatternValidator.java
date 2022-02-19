package com.flab.planb.annotation;

import java.time.LocalTime;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimePatternValidator implements ConstraintValidator<TimePattern, LocalTime> {

    private String pattern;

    @Override
    public void initialize(TimePattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        return Pattern.compile(pattern).matcher(value.toString()).matches();
    }
}
