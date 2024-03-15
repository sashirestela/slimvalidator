package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;

/**
 * Checks that the type of an object is one of a list of candidate types.
 */
public class ObjectTypeListValidator implements ConstraintValidator<ObjectType.List, Object> {

    private ObjectType[] objectTypeList;

    @Override
    public void initialize(ObjectType.List annotation) {
        objectTypeList = annotation.value().clone();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        var validator = new ObjectTypeValidator();
        for (var objectType : objectTypeList) {
            validator.initialize(objectType);
            if (validator.isValid(value)) {
                return true;
            }
        }
        return false;
    }

}
