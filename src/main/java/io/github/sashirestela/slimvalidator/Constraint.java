package io.github.sashirestela.slimvalidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark other annotations as constraints.
 */
@Documented
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {

    /**
     * Defines what class is going to execute the validation for a constraint. Mandatory.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends ConstraintValidator> validatedBy();

}
