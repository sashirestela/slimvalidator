package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;

import java.util.Collection;

public class ObjectTypeValidator implements ConstraintValidator<ObjectType, Object> {

    private Class<?> baseClass;
    private boolean firstGroup;
    private boolean secondGroup;
    private int maxSize;

    @Override
    public void initialize(ObjectType annotation) {
        baseClass = annotation.baseClass();
        firstGroup = annotation.firstGroup();
        secondGroup = annotation.secondGroup();
        maxSize = annotation.maxSize();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!firstGroup) {
            return baseClass.isInstance(value);
        } else if (!secondGroup) {
            return Collection.class.isInstance(value)
                    && baseClass.isInstance(((Collection<?>) value).iterator().next())
                    && (maxSize == 0 || ((Collection<?>) value).size() <= maxSize);
        } else {
            return Collection.class.isInstance(value)
                    && Collection.class.isInstance(((Collection<?>) value).iterator().next())
                    && baseClass
                            .isInstance(((Collection<?>) ((Collection<?>) value).iterator().next()).iterator().next())
                    && (maxSize == 0 || ((Collection<?>) value).size() <= maxSize);
        }
    }

}
