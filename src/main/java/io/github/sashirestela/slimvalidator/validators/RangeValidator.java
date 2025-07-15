package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Checks that a value is within a closed range. Applies to fields of any numeric type.
 */
public class RangeValidator implements ConstraintValidator<Range, Object> {

    private double min;
    private double max;
    private boolean isVariableType;

    @Override
    public void initialize(Range annotation) {
        min = annotation.min();
        max = annotation.max();
        isVariableType = annotation.isVariableType();
        if (min == -Double.MAX_VALUE && max == Double.MAX_VALUE) {
            throw new ValidationException("In Range constraint, min or max must be set.");
        }
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
        if (min > -Double.MAX_VALUE) {
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
        try {
            double number = NumberFormat.getInstance().parse(value.toString()).doubleValue();
            return (number >= min && number <= max);
        } catch (ParseException e) {
            if (isVariableType) {
                return true;
            } else {
                throw new ValidationException("Cannot get a number from {0}.", value.getClass().getSimpleName(), null);
            }
        }
    }

}
