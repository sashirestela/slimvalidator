package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.validators.RequiredIfNullValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RequiredIfNullValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredIfNull {

    String[] fields();

    String dependsOn();

}
