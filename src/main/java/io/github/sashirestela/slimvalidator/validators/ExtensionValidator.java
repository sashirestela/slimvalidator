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

    private String getExtension(Object value) throws IllegalArgumentException {
        String fileName;

        if (value instanceof File) {
            fileName = ((File) value).getName();
        } else if (value instanceof Path) {
            fileName = ((Path) value).getFileName().toString();
        } else {
            throw new ValidationException("Input must be a File or Path.");
        }

        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationException("File name is null or empty.");
        }

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == fileName.length() - 1) {
            throw new ValidationException("No valid file extension found.");
        }

        return fileName.substring(lastDotIndex + 1);
    }
}
