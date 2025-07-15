package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.exception.ValidationException;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Checks that the file extension is one of an expected list. Applies to {@link java.nio.file.Path
 * Path} and {@link java.io.File File} objects.
 */
public class ExtensionValidator implements ConstraintValidator<Extension, Object> {

    private String[] extensions;
    private boolean isVariableType;

    @Override
    public void initialize(Extension annotation) {
        extensions = annotation.value().clone();
        isVariableType = annotation.isVariableType();
    }

    @Override
    public String getMessage() {
        var message = new StringBuilder();
        message.append("extension must be one of ").append(Arrays.toString(extensions));
        return message.toString();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        try {
            return Arrays.stream(extensions).anyMatch(ext -> ext.equals(getExtension(value)));
        } catch (IllegalArgumentException e) {
            if (isVariableType) {
                return true;
            } else {
                throw new ValidationException("Object must be a File or Path.");
            }
        }
    }

    private String getExtension(Object value) {
        String fileName;

        if (value instanceof File) {
            fileName = ((File) value).getName();
        } else if (value instanceof Path) {
            fileName = ((Path) value).getFileName().toString();
        } else {
            throw new IllegalArgumentException();
        }

        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationException("File name is null or empty.");
        }

        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == fileName.length() - 1) {
            throw new ValidationException("No valid file extension found.");
        }

        return fileName.substring(lastDotIndex + 1);
    }

}
