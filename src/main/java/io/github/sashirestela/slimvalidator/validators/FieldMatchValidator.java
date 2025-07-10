package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.FieldMatch;
import io.github.sashirestela.slimvalidator.util.Reflect;

import java.util.Objects;

/**
 * Checks that a first field matches a second field. Applies to fields of any type.
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String first;
    private String second;

    @Override
    public void initialize(FieldMatch annotation) {
        first = annotation.first();
        second = annotation.second();
    }

    @Override
    public String getMessage() {
        var message = new StringBuilder();
        message.append(first).append(" and ").append(second).append(" must match.");
        return message.toString();
    }

    @Override
    public boolean isValid(Object value) {
        var firstValue = Reflect.getValue(value, first);
        var secondValue = Reflect.getValue(value, second);
        return Objects.deepEquals(firstValue, secondValue);
    }

}
