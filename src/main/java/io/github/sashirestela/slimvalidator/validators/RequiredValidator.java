package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Required;

public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public boolean isValid(Object value) {
        return (value != null);
    }

}
