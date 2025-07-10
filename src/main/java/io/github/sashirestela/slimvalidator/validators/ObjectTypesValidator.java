package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.ObjectTypes;

/**
 * Checks that the type of an object matches one of multiple ObjectType configurations. This
 * validator allows for multiple schema patterns to be applied to the same field.
 */
public class ObjectTypesValidator implements ConstraintValidator<ObjectTypes, Object> {

    public static final String MSG_PREFIX = "type must be one of ";
    public static final String MSG_POSTFIX = ".";

    private ObjectType[] objectTypeList;

    @Override
    public void initialize(ObjectTypes annotation) {
        objectTypeList = annotation.value().clone();
    }

    @Override
    public String getMessage() {
        var message = new StringBuilder(MSG_PREFIX);
        var itemValidator = new ObjectTypeValidator();
        var itemMessage = "";
        for (int i = 0; i < objectTypeList.length; i++) {
            itemValidator.initialize(objectTypeList[i]);
            itemMessage = itemValidator.getMessage().replace(MSG_PREFIX, "");
            itemMessage = itemMessage.replace(MSG_POSTFIX, "");
            message.append(itemMessage);
            if (i < objectTypeList.length - 1) {
                message.append(" or ");
            } else {
                message.append(MSG_POSTFIX);
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
