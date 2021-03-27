package com.example.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class} // 指定验证规则类
)
public @interface IsMobile {

    // 注解的内容可以为空
    boolean required() default false;

    // 默认提示信息
    String message() default "{手机号码格式错误}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
