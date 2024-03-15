package io.github.sashirestela.slimvalidator.exception;

import java.text.MessageFormat;
import java.util.Arrays;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Object... parameters) {
        super(MessageFormat.format(message, Arrays.copyOfRange(parameters, 0, parameters.length - 1)),
                (Throwable) parameters[parameters.length - 1]);
    }

}
