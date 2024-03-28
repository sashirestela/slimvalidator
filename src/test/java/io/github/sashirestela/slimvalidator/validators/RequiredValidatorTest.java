package io.github.sashirestela.slimvalidator.validators;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequiredValidatorTest {

    @Test
    void shouldReturnExpectedResultWhenEvaluatingDataPassed() {
        Object[][] data = {
                { null, false },
                { "", true },
                { "Text", true },
                { List.of(), false },
                { List.of(1, 2), true },
                { Map.of(), false },
                { Map.of("key", "value"), true },
                { new Object[] {}, false },
                { new Object[] { 1, 2, 3 }, true }
        };
        for (Object[] value : data) {
            var validator = new RequiredValidator();
            var actualResult = validator.isValid(value[0]);
            var expectedResult = (boolean) value[1];
            assertEquals(expectedResult, actualResult);
        }
    }

}
