package com.nerdysoft.competencymatrix.validation.validator;

import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.validation.annotation.PriorityAllow;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PriorityValidator implements ConstraintValidator<PriorityAllow, Priority> {

    private List<Priority> priorities = List.of(Priority.LOW, Priority.MEDIUM);

    @Override
    public boolean isValid(Priority priority, ConstraintValidatorContext constraintValidatorContext) {
        return priorities.contains(priority);
    }
}
