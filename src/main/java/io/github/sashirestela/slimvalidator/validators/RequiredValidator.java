package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Required;

import java.util.Collection;
import java.util.Map;

/**
 * Checks that a value is not null or not empty in case of groups. Applies to fields of any type.
 */
public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public void initialize(Required annotation) {
        // No fields in this annotation
    }

    @Override
    public boolean isValid(Object value) {
        return (value != null && !isEmpty(value));
    }

    @Override
    public String getMessage() {
        return "must have a value.";
    }

    private boolean isEmpty(Object value) {
        int size = 1;
        if (value instanceof Collection) {
            size = ((Collection<?>) value).size();
        } else if (value instanceof Map) {
            size = ((Map<?, ?>) value).size();
        } else if (value.getClass().isArray()) {
            size = ((Object[]) value).length;
        }
        return (size == 0);
    }

}
