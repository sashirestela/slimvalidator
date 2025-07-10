package io.github.sashirestela.slimvalidator.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonTest {

    @Test
    void shouldConcatArraysWhenTwoArePassed() {
        Object[][][] data = {
                { { 1, 2 }, { 3, 4 }, { 1, 2, 3, 4 } },
                { { "a", "b", "c" }, {}, { "a", "b", "c" } },
                { {}, { 11.1, 22.2 }, { 11.1, 22.2 } }
        };
        for (Object[][] array : data) {
            var actualArray = Common.concatArrays(array[0], array[1]);
            var expectedArray = array[2];
            assertArrayEquals(expectedArray, actualArray);
        }
    }

    @Test
    void shouldReturnTrueWhenClassIsPrimitiveOrWrapper() {
        Object[] data = {
                'x', 5, 7l, 3.14f, 9.5, true, "qwerty", Character.valueOf('x'), Byte.valueOf("1"), Short.valueOf("3"),
                Integer.valueOf(6), Long.valueOf(9l), Float.valueOf(8.2f), Double.valueOf(9.4), Boolean.valueOf(false)
        };
        for (Object value : data) {
            var actualResult = Common.isPrimitiveOrWrapper(value);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnFalseWhenClassIsNotPrimitiveOrWrapper() {
        Object[] data = {
                null, Arrays.asList(1, 2, 3), Map.of("key", 17.65), new Exception()
        };
        for (Object value : data) {
            var actualResult = Common.isPrimitiveOrWrapper(value);
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }
    }

    static enum Sample {
        ONE;
    }

}
