package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.ObjectTypes;

/**
 * Checks that the type of an object matches one of multiple ObjectType configurations. This
 * validator allows for multiple schema patterns to be applied to the same field.
 */
public class ObjectTypesValidator implements ConstraintValidator<ObjectTypes, Object> {

    private ObjectType[] objectTypeList;

    @Override
    public void initialize(ObjectTypes annotation) {
        objectTypeList = annotation.value().clone();
    }

    @Override
    public String getMessage() {
        var message = new StringBuilder("type must be one of ");
        var itemValidator = new ObjectTypeValidator();
        for (int i = 0; i < objectTypeList.length; i++) {
            itemValidator.initialize(objectTypeList[i]);
            message.append(itemValidator.getMessage());
            if (i < objectTypeList.length - 1) {
                message.append(" or ");
            } else {
                message.append(".");
            }
        }
        return message.toString();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        var itemValidator = new ObjectTypeValidator();
        for (var objectType : objectTypeList) {
            itemValidator.initialize(objectType);
            if (itemValidator.isValid(value)) {
                return true;
            }
        }
        return false;
    }

}
