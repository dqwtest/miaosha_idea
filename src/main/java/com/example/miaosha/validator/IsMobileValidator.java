package com.example.miaosha.validator;

import com.example.miaosha.util.ValidateUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required) {
            return ValidateUtil.isMobile(s);
        } else {
            if(StringUtils.isEmpty(s)) {
                return true;
            } else {
                return ValidateUtil.isMobile(s);
            }
        }
    }
}
