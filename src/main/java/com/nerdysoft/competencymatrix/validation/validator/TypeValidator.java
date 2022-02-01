package com.nerdysoft.competencymatrix.validation.validator;

import com.nerdysoft.competencymatrix.entity.enums.Priority;
import com.nerdysoft.competencymatrix.entity.enums.Type;
import com.nerdysoft.competencymatrix.validation.annotation.TypeAllow;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TypeValidator implements ConstraintValidator<TypeAllow, Type> {

    private List<Type> types = List.of(Type.SOFT_SKILL, Type.HARD_SKILL);

    @Override
    public boolean isValid(Type type, ConstraintValidatorContext constraintValidatorContext) {
        return types.contains(type);
    }
}
