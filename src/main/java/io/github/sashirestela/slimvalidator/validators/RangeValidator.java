package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.text.DecimalFormat;

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
        if (min >= max) {
            throw new ValidationException("In Range constraint, min must be less than max.");
        }
    }

    @Override
    public String getMessage() {
        var decFormat = new DecimalFormat("#.##");
        decFormat.setDecimalSeparatorAlwaysShown(false);
        var message = new StringBuilder();
        message.append("must be");
        if (min < Double.MAX_VALUE) {
            message.append(" at least ").append(decFormat.format(min));
        }
        if (max < Double.MAX_VALUE) {
            message.append(" at most ").append(decFormat.format(max));
        }
        message.append(".");
        return message.toString();
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
