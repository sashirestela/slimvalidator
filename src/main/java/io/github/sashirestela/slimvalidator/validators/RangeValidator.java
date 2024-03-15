package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

/**
 * Checks that a value is within a closed range. Applies to fields of any numeric type.
 */
public class RangeValidator implements ConstraintValidator<Range, Object> {

    private double min;
    private double max;

    @Override
    public void initialize(Range annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        double number = getNumber(value);
        return (number >= min && number <= max);
    }

    private double getNumber(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Float) {
            return ((Float) value).doubleValue();
        } else if (value instanceof Double) {
            return ((Double) value).doubleValue();
        } else {
            throw new ValidationException("Cannot get a number from {0}.", value.getClass().getName(), null);
        }
    }

}
