package io.github.sashirestela.slimvalidator;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<A extends Annotation, T> {

    default void initialize(A annotation) {
    }

    boolean isValid(T value);

}
