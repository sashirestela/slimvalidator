package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.validators.SizeValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = SizeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    boolean isVariableType() default false;

}
