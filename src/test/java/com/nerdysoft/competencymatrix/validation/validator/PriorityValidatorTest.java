package com.nerdysoft.competencymatrix.validation.validator;

import com.nerdysoft.competencymatrix.entity.enums.Priority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PriorityValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private PriorityValidator validator;

    @Test
    void isValid() {
        boolean inValid = validator.isValid(Priority.HIGH, context);
        boolean valid = validator.isValid(Priority.LOW, context);

        assertTrue(valid);
        assertFalse(inValid);
    }
}