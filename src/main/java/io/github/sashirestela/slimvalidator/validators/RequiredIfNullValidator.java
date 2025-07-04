package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.RequiredIfNull;
import io.github.sashirestela.slimvalidator.util.Reflect;

import java.util.Arrays;
import java.util.Objects;

/**
 * Checks that all fields in a list are not null if the dependsOn field is null. Applies to fields
 * of any type.
 */
public class RequiredIfNullValidator implements ConstraintValidator<RequiredIfNull, Object> {

    private String[] fields;
    private String dependsOn;

    @Override
    public void initialize(RequiredIfNull annotation) {
        fields = annotation.fields();
        dependsOn = annotation.dependsOn();
    }

    @Override
    public boolean isValid(Object value) {
        var dependsOnValue = Reflect.getValue(value, dependsOn);
        if (Objects.isNull(dependsOnValue)) {
            return Arrays.asList(fields)
                    .stream()
                    .map(field -> Reflect.getValue(value, field))
                    .allMatch(Objects::nonNull);
        }
        return true;
    }

}
