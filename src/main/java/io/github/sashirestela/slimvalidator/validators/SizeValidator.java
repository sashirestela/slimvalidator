package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Size;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.util.Collection;
import java.util.Map;

/**
 * Checks that a text's length or a group's size is within a closed range. Applies to fields of
 * type: String, Collection, Map or Object array.
 */
public class SizeValidator implements ConstraintValidator<Size, Object> {

    private int min;
    private int max;
    private boolean isVariableType;

    @Override
    public void initialize(Size annotation) {
        min = annotation.min();
        max = annotation.max();
        isVariableType = annotation.isVariableType();
        if (min > max) {
            throw new ValidationException("In Size constraint, min must be less or equal than max.");
        }
    }

    @Override
    public String getMessage() {
        var message = new StringBuilder();
        message.append("size must be");
        if (min > 0) {
            message.append(" at least ").append(min);
        }
        if (max < Integer.MAX_VALUE) {
            message.append(" at most ").append(max);
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
            int size = getSize(value, isVariableType);
            return (size >= min && size <= max);
        } catch (IllegalArgumentException e) {
            if (isVariableType) {
                return true;
            } else {
                throw new ValidationException("Cannot get a size from {0}.", value.getClass().getSimpleName(), null);
            }
        }
    }

    private int getSize(Object value, boolean isVariableType) {
        if (value instanceof String) {
            return ((String) value).length();
        } else {
            if (isVariableType) {
                throw new IllegalArgumentException();
            } else {
                if (value instanceof Collection) {
                    return ((Collection<?>) value).size();
                } else if (value instanceof Map) {
                    return ((Map<?, ?>) value).size();
                } else if (value.getClass().isArray()) {
                    return ((Object[]) value).length;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

}
