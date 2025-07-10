package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.validators.RangeValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

    double min() default -Double.MAX_VALUE;

    double max() default Double.MAX_VALUE;

}
