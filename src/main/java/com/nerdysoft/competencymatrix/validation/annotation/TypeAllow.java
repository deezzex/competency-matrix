package com.nerdysoft.competencymatrix.validation.annotation;

import com.nerdysoft.competencymatrix.validation.validator.PriorityValidator;
import com.nerdysoft.competencymatrix.validation.validator.TypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = TypeValidator.class)
public @interface TypeAllow {
    String message() default "Type not allow";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
