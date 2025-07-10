package io.github.sashirestela.slimvalidator;

/**
 * Detail of every constraint violation.
 */
public class ConstraintViolation {

    private final Object value;
    private final String name;
    private final String message;

    public ConstraintViolation(Object value, String name, String message) {
        this.value = value;
        this.name = name;
        this.message = message;
    }

    public Object getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

}
