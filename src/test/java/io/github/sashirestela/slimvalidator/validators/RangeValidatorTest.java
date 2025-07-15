package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RangeValidatorTest {

    @Test
    void shouldReturnTrueWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { null, Sample.range(2, 10) },
                { Integer.valueOf(7), Sample.range(2, 10) },
                { Long.valueOf(7), Sample.range(2, 10) },
                { Float.valueOf(7.5f), Sample.range(2.0, 10.0) },
                { Double.valueOf(7.5), Sample.range(2.0, 10.0) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnFalseWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { Integer.valueOf(13), Sample.range(2, 10) },
                { Long.valueOf(1), Sample.range(2, 10) },
                { Float.valueOf(27.5f), Sample.range(2.0, 10.0) },
                { Double.valueOf(1.5), Sample.range(2.0, 10.0) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnTrueWhenValidatingBoundaryValues() {
        Object[][] data = {
                { Integer.valueOf(2), Sample.range(2, 10) },
                { Integer.valueOf(10), Sample.range(2, 10) },
                { Double.valueOf(2.0), Sample.range(2.0, 10.0) },
                { Double.valueOf(10.0), Sample.range(2.0, 10.0) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnTrueWhenValidatingOnlyMaxConstraint() {
        Object[][] data = {
                { Integer.valueOf(5), Sample.range(10) },
                { Integer.valueOf(10), Sample.range(10) },
                { Double.valueOf(-1000.0), Sample.range(10) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnTrueWhenValidatingStringNumbers() {
        Object[][] data = {
                { "5", Sample.range(2, 10) },
                { "7.5", Sample.range(2.0, 10.0) },
                { "2", Sample.range(2, 10) },
                { "10", Sample.range(2, 10) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnTrueWhenValidatingNonNumericWithVariableType() {
        Object[][] data = {
                { "abc", Sample.range(2, 10, true) },
                { Boolean.TRUE, Sample.range(2, 10, true) },
                { new Object(), Sample.range(2, 10, true) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldThrowExceptionWhenValidatingNonNumericWithoutVariableType() {
        Object[][] data = {
                { "abc", Sample.range(2, 10) },
                { Boolean.TRUE, Sample.range(2, 10) },
                { new Object(), Sample.range(2, 10) }
        };
        for (Object[] value : data) {
            var validator = new RangeValidator();
            var annotation = (Range) value[1];
            validator.initialize(annotation);
            assertThrows(ValidationException.class, () -> validator.isValid(value[0]));
        }
    }

    @Test
    void shouldThrownExceptionWhenMinIsGreaterThanMax() {
        var validator = new RangeValidator();
        var annotation = Sample.range(1.5, 0.5);
        var exception = assertThrows(ValidationException.class, () -> validator.initialize(annotation));
        var actualMessage = exception.getMessage();
        var expectedMessage = "In Range constraint, min must be less than max.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionWhenMinEqualsMax() {
        var validator = new RangeValidator();
        var annotation = Sample.range(5.0, 5.0);
        var exception = assertThrows(ValidationException.class, () -> validator.initialize(annotation));
        var actualMessage = exception.getMessage();
        var expectedMessage = "In Range constraint, min must be less than max.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrownExceptionWhenBothMinAndMaxAreDefault() {
        var validator = new RangeValidator();
        var annotation = Sample.range(-Double.MAX_VALUE, Double.MAX_VALUE);
        var exception = assertThrows(ValidationException.class, () -> validator.initialize(annotation));
        var actualMessage = exception.getMessage();
        var expectedMessage = "In Range constraint, min or max must be set.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForBothMinAndMax() {
        var validator = new RangeValidator();
        var annotation = Sample.range(2.5, 10.75);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "must be at least 2.5 at most 10.75.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForOnlyMax() {
        var validator = new RangeValidator();
        var annotation = Sample.range(10.5);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "must be at most 10.5.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForIntegerValues() {
        var validator = new RangeValidator();
        var annotation = Sample.range(2.0, 10.0);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "must be at least 2 at most 10.";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldGenerateCorrectMessageForOnlyMin() {
        var validator = new RangeValidator();
        var annotation = Sample.range(2.5, Double.MAX_VALUE);
        validator.initialize(annotation);
        var actualMessage = validator.getMessage();
        var expectedMessage = "must be at least 2.5.";
        assertEquals(expectedMessage, actualMessage);
    }

    static class Sample {

        static Range range(double max) {
            return range(-Double.MAX_VALUE, max, false);
        }

        static Range range(double min, double max) {
            return range(min, max, false);
        }

        static Range range(double min, double max, boolean isVariableType) {
            return new Range() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Range.class;
                }

                @Override
                public double min() {
                    return min;
                }

                @Override
                public double max() {
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
