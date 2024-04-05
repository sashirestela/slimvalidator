package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtensionValidatorTest {

    String fileName = "src/test/resources/sample.txt";

    @Test
    void shouldReturnTrueWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { null, Sample.extension(null) },
                { new File(fileName), Sample.extension(new String[] { "doc", "csv", "txt" }) },
                { Paths.get(fileName), Sample.extension(new String[] { "doc", "csv", "txt" }) }
        };
        for (Object[] value : data) {
            var validator = new ExtensionValidator();
            var annotation = (Extension) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnFalseWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { new File(fileName), Sample.extension(new String[] { "jpg", "gif", "bmp" }) },
                { Paths.get(fileName), Sample.extension(new String[] { "jpg", "gif", "bmp" }) }
        };
        for (Object[] value : data) {
            var validator = new ExtensionValidator();
            var annotation = (Extension) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldThrownExceptionWhenNonExpectedObjectIsPassed() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        var exception = assertThrows(ValidationException.class, () -> validator.isValid("filename.mp3"));
        var actualMessage = exception.getMessage();
        var expectedMessage = "Cannot get a extension from java.lang.String.";
        assertEquals(expectedMessage, actualMessage);
    }

    static class Sample {

        static Extension extension(String[] value) {
            return new Extension() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Extension.class;
                }

                @Override
                public String message() {
                    return "";
                }

                @Override
                public String[] value() {
                    return value;
                }

            };

        }

    }

}
