package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Size;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.util.Collection;
import java.util.Map;

public class SizeValidator implements ConstraintValidator<Size, Object> {

    private int min;
    private int max;

    @Override
    public void initialize(Size annotation) {
        min = annotation.min();
        max = annotation.max();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        int length = getSize(value);
        return (length >= min && length <= max);
    }

    private int getSize(Object value) {
        if (String.class.isInstance(value)) {
            return ((String) value).length();
        } else if (Collection.class.isInstance(value)) {
            return ((Collection<?>) value).size();
        } else if (Map.class.isInstance(value)) {
            return ((Map<?, ?>) value).size();
        } else if (value.getClass().isArray()) {
            return ((Object[]) value).length;
        } else {
            throw new ValidationException("Cannot get a size from {0}.", value.getClass().getName(), null);
        }
    }

}
