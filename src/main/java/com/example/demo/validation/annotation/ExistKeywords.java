package com.example.demo.validation.annotation;

import com.example.demo.validation.validator.KeywordsExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = KeywordsExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistKeywords {
    String message() default "해당하는 키워드가 존재하지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
