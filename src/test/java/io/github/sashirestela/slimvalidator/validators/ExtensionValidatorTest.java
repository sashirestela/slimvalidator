package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.Extension;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtensionValidatorTest {

    String fileName = "src/test/resources/sample.txt";

    @Test
    void shouldReturnTrueWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { null, Sample.extension(new String[] { "doc", "txt" }) },
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
        var expectedMessage = "Object must be a File or Path.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionForHiddenFilesInFile() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File(".hiddenfile");
        var exception = assertThrows(ValidationException.class, () -> validator.isValid(file));
        var actualMessage = exception.getMessage();
        var expectedMessage = "No valid file extension found.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionForHiddenFilesInPath() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        Path path = new File(".hiddenfile").toPath();
        var exception = assertThrows(ValidationException.class, () -> validator.isValid(path));
        var actualMessage = exception.getMessage();
        var expectedMessage = "No valid file extension found.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionForEmptyFilenameInFile() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File("");
        var exception = assertThrows(ValidationException.class, () -> validator.isValid(file));
        var actualMessage = exception.getMessage();
        var expectedMessage = "File name is null or empty.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionForEmptyFilenameInPath() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        Path path = new File("").toPath();
        var exception = assertThrows(ValidationException.class, () -> validator.isValid(path));
        var actualMessage = exception.getMessage();
        var expectedMessage = "File name is null or empty.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldSucceedWithExtraPeriodsInFilename() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File("/a/b/c/foo.bar.baz.mp3");
        assertEquals(Boolean.TRUE, validator.isValid(file));
    }

    @Test
    void shouldSucceedWithExtraPeriodsInPath() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File("a/b/c/foo.bar.baz.mp3");
        assertEquals(Boolean.TRUE, validator.isValid(file.toPath()));
    }

    @Test
    void shouldReturnFalseWithExtraPeriodsInFile() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File("a/b/c/foo.mp3.wav.txt");
        assertEquals(Boolean.FALSE, validator.isValid(file));
    }

    @Test
    void shouldReturnFalseWithExtraPeriodsInPath() {
        var validator = new ExtensionValidator();
        var annotation = Sample.extension(new String[] { "mp3", "wav" });
        validator.initialize(annotation);
        File file = new File("a/b/c/foo.mp3.wav.txt");
        assertEquals(Boolean.FALSE, validator.isValid(file.toPath()));
    }

    @Test
    void shouldGenerateCorrectMessages() {
        var validator = new ExtensionValidator();
        validator.initialize(Sample.extension(new String[] { "jpg", "gif", "bmp" }));
        assertEquals("extension must be one of [jpg, gif, bmp]", validator.getMessage());
    }

    static class Sample {

        static Extension extension(String[] value) {
            return new Extension() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Extension.class;
                }

                @Override
                public String[] value() {
                    return value;
                }

            };

        }

    }

}
