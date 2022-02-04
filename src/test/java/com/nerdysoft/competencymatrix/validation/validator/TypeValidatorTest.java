package com.nerdysoft.competencymatrix.validation.validator;

import com.nerdysoft.competencymatrix.entity.enums.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TypeValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private TypeValidator validator;

    @Test
    void isValid() {
        boolean valid1 = validator.isValid(Type.HARD_SKILL, context);
        boolean valid2 = validator.isValid(Type.SOFT_SKILL, context);

        assertTrue(valid1);
        assertTrue(valid2);
    }
}