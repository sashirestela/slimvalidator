package io.github.sashirestela.slimvalidator;

import java.lang.annotation.Annotation;

/**
 * Defines the behavior of every validator class.
 */
public interface ConstraintValidator<A extends Annotation, T> {

    /**
     * Set up any state using the constraint annotation's fields.
     * 
     * @param annotation Constraint annotation.
     */
    void initialize(A annotation);

    /**
     * Prepare and get the message from this validator.
     * 
     * @return The prepared message.
     */
    String getMessage();

    /**
     * Execute the object validation against the constraint.
     * 
     * @param value Object to be validated.
     * @return The result of the validation.
     */
    boolean isValid(T value);

}
