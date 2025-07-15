package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.Size;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SizeValidatorTest {

    @Test
    void shouldReturnTrueWhenValidatingObjectAgainstAnnotation() {
        Object[][] data = {
                // null values should always be valid
                { null, Sample.size(2, 10) },

                // String validations
                { "hello", Sample.size(2, 10) },
                { "hi", Sample.size(2, 10) },
                { "1234567890", Sample.size(2, 10) },

                // Collection validations
                { Arrays.asList("a", "b", "c"), Sample.size(2, 10) },
                { Arrays.asList("a", "b"), Sample.size(2, 10) },
                { Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"), Sample.size(2, 10) },

                // Map validations
                { createMap(3), Sample.size(2, 10) },
                { createMap(2), Sample.size(2, 10) },
                { createMap(10), Sample.size(2, 10) },

                // Array validations
                { new String[] { "a", "b", "c" }, Sample.size(2, 10) },
                { new String[] { "a", "b" }, Sample.size(2, 10) },
                { new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" }, Sample.size(2, 10) },

                // Edge cases with min=0
                { "", Sample.size(0, 5) },
                { Arrays.asList(), Sample.size(0, 5) },
                { new HashMap<>(), Sample.size(0, 5) },
                { new String[] {}, Sample.size(0, 5) },

                // Edge cases with max=Integer.MAX_VALUE
                { "very long string", Sample.size(0, Integer.MAX_VALUE) },
                { Arrays.asList("a", "b", "c", "d", "e"), Sample.size(0, Integer.MAX_VALUE) }
        };

        for (Object[] value : data) {
            var validator = new SizeValidator();
            var annotation = (Size) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnFalseWhenValidatingObjectAgainstAnnotation() {
        Object[][] data = {
                // String validations - too short
                { "a", Sample.size(2, 10) },
                { "", Sample.size(2, 10) },

                // String validations - too long
                { "12345678901", Sample.size(2, 10) },
                { "this is a very long string", Sample.size(2, 10) },

                // Collection validations - too short
                { Arrays.asList("a"), Sample.size(2, 10) },
                { Arrays.asList(), Sample.size(2, 10) },

                // Collection validations - too long
                { Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"), Sample.size(2, 10) },

                // Map validations - too short
                { createMap(1), Sample.size(2, 10) },
                { createMap(0), Sample.size(2, 10) },

                // Map validations - too long
                { createMap(11), Sample.size(2, 10) },

                // Array validations - too short
                { new String[] { "a" }, Sample.size(2, 10) },
                { new String[] {}, Sample.size(2, 10) },

                // Array validations - too long
                { new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k" }, Sample.size(2, 10) }
        };

        for (Object[] value : data) {
            var validator = new SizeValidator();
            var annotation = (Size) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldThrownExceptionWhenUnsupportedTypeIsPassed() {
        var validator = new SizeValidator();
        var annotation = Sample.size(2, 10);
        validator.initialize(annotation);
        var exception = assertThrows(ValidationException.class, () -> validator.isValid(123));
        var actualMessage = exception.getMessage();
        var expectedMessage = "Cannot get a size from Integer.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionWhenMinIsGreaterThanMax() {
        var validator = new SizeValidator();
        var annotation = Sample.size(10, 5);
        var exception = assertThrows(ValidationException.class, () -> validator.initialize(annotation));
        var actualMessage = exception.getMessage();
        var expectedMessage = "In Size constraint, min must be less or equal than max.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForMinOnly() {
        var validator = new SizeValidator();
        var annotation = Sample.size(5, Integer.MAX_VALUE);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "size must be at least 5.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForMaxOnly() {
        var validator = new SizeValidator();
        var annotation = Sample.size(0, 10);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "size must be at most 10.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForMinAndMax() {
        var validator = new SizeValidator();
        var annotation = Sample.size(5, 10);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "size must be at least 5 at most 10.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldHandlePrimitiveArrays() {
        var validator = new SizeValidator();
        var annotation = Sample.size(2, 5);
        validator.initialize(annotation);

        // Test with primitive arrays converted to Object arrays
        var intArray = new Integer[] { 1, 2, 3 };
        var actualResult = validator.isValid(intArray);
        var expectedResult = true;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldReturnTrueWhenValidatingNonExpectedTypesWithVariableType() {
        Object[][] data = {
                { 123.45, Sample.size(2, 10, true) },
                { Boolean.TRUE, Sample.size(2, 10, true) },
                { new Object(), Sample.size(2, 10, true) },
                { List.of("a", "b", "c"), Sample.size(2, 10, true) }
        };
        for (Object[] value : data) {
            var validator = new SizeValidator();
            var annotation = (Size) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    private static Map<String, String> createMap(int size) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put("key" + i, "value" + i);
        }
        return map;
    }

    static class Sample {

        static Size size(int min, int max) {
            return size(min, max, false);
        }

        static Size size(int min, int max, boolean isVariableType) {
            return new Size() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Size.class;
                }

                @Override
                public int min() {
                    return min;
                }

                @Override
                public int max() {
                    return max;
                }

                @Override
                public boolean isVariableType() {
                    return isVariableType;
                }

            };
        }

    }

}
