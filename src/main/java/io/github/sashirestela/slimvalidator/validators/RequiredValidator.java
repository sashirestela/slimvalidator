package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Required;

/**
 * Checks that a value is not null. Applies to fields of any type.
 */
public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public boolean isValid(Object value) {
        return (value != null);
    }

}
