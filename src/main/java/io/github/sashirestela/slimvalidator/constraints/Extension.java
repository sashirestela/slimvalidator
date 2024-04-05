package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.validators.ExtensionValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExtensionValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Extension {

    String message() default "extension must be one of {value}.";

    String[] value();

}
