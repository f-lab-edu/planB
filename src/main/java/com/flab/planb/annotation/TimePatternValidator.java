package com.flab.planb.annotation;

import java.time.LocalTime;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimePatternValidator implements ConstraintValidator<TimePattern, LocalTime> {

    private Pattern pattern;

    @Override
    public void initialize(TimePattern constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        return pattern.matcher(value.toString()).matches();
    }
}
