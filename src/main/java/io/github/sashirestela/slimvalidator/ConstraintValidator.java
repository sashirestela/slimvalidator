package io.github.sashirestela.slimvalidator;

import java.lang.annotation.Annotation;

/**
 * Defines the behavior of every validator class.
 */
public interface ConstraintValidator<A extends Annotation, T> {

    /**
     * Set up any state using the constraint annotation's fields. It is optional to implement it.
     * 
     * @param annotation Constraint annotation.
     */
    default void initialize(A annotation) {
    }

    /**
     * Prepare and get the message from this validator. Implement it when major logic is needed.
     * 
     * @return The prepared message.
     */
    default String getMessage() {
        return null;
    }

    /**
     * Execute the object validation against the constraint.
     * 
     * @param value Object to be validated.
     * @return The result of the validation.
     */
    boolean isValid(T value);

}
