package com.example.demo.validation.annotation;

import com.example.demo.validation.validator.EnumValidator;
import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
public @interface CheckEnum {
    String message() default "Enum에 없는 값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}