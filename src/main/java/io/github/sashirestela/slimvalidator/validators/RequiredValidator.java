package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Required;

import java.util.Collection;
import java.util.Map;

/**
 * Checks that a value is not null. Applies to fields of any type.
 */
public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        } else {
            return getGroupSize(value) != 0;
        }
    }

    private int getGroupSize(Object value) {
        if (value instanceof Collection) {
            return ((Collection<?>) value).size();
        } else if (value instanceof Map) {
            return ((Map<?, ?>) value).size();
        } else if (value.getClass().isArray()) {
            return ((Object[]) value).length;
        } else {
            return -1;
        }
    }

}
