package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RangeValidatorTest {

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
                { Long.valueOf(13), Sample.range(2, 10) },
                { Float.valueOf(27.5f), Sample.range(2.0, 10.0) },
                { Double.valueOf(27.5), Sample.range(2.0, 10.0) }
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
    void shouldThrownExceptionWhenNonNumericValueIsPassed() {
        var validator = new RangeValidator();
        var annotation = Sample.range(2, 10);
        validator.initialize(annotation);
        var exception = assertThrows(ValidationException.class, () -> validator.isValid("text"));
        var actualMessage = exception.getMessage();
        var expectedMessage = "Cannot get a number from java.lang.String.";
        assertEquals(expectedMessage, actualMessage);
    }

    static class Sample {

        static Range range(double min, double max) {
            return new Range() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Range.class;
                }

                @Override
                public String message() {
                    return "";
                }

                @Override
                public double min() {
                    return min;
                }

                @Override
                public double max() {
                    return max;
                }

            };
        }

    }

}
