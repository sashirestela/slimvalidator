package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class ExtensionValidator implements ConstraintValidator<Extension, Object> {

    private String[] extensions;

    @Override
    public void initialize(Extension annotation) {
        extensions = annotation.value();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(extensions).anyMatch(ext -> ext.equals(getExtension(value)));
    }

    private String getExtension(Object value) {
        if (value instanceof Path) {
            return ((Path) value).toString().split("\\.")[1];
        } else if (value instanceof File) {
            return ((File) value).getName().split("\\.")[1];
        } else {
            throw new ValidationException("Cannot get a extension from {0}.", value.getClass().getName(), null);
        }
    }

}
