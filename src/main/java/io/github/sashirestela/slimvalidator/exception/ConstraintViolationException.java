package io.github.sashirestela.slimvalidator.exception;

import io.github.sashirestela.slimvalidator.ConstraintViolation;

import java.util.List;
import java.util.stream.Collectors;

public class ConstraintViolationException extends RuntimeException {

    public ConstraintViolationException(List<ConstraintViolation> violations) {
        super(violations.stream()
                .map(v -> prepareName(v.getName()) + v.getMessage())
                .collect(Collectors.joining("\n")));
    }

    private static String prepareName(String name) {
        return name.isBlank() ? "" : name.concat(" ");
    }

}
