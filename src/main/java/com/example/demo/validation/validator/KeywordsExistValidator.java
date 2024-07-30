package com.example.demo.validation.validator;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.repository.KeywordRepository;
import com.example.demo.validation.annotation.ExistKeywords;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeywordsExistValidator implements ConstraintValidator<ExistKeywords, List<Long>> {
    private final KeywordRepository keywordRepository;
    @Override
    public void initialize(ExistKeywords constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<Long> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("키워드는 최소 3개 이상 선택해야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        if (values.size() < 3 || values.size() > 3) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("키워드는 3개 선택해야 합니다.")
                    .addConstraintViolation();
            return false;
        }

        boolean isValid = values.stream()
                .allMatch(keywordRepository::existsById);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.KEYWORD_NOT_FOUND.toString())
                    .addConstraintViolation();
        }

        return isValid;
    }
}