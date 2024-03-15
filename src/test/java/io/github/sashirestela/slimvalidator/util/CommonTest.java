package io.github.sashirestela.slimvalidator.util;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.NullType;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonTest {

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

    @Test
    void shouldReturnTrueWhenValueIsNotDefaultValueByAnnotMethodType() {
        Object[] data = {
                true, "test", 3.1416, 17, Void.class, Sample.ONE, new String[] { "1", "2" }
        };
        for (Object value : data) {
            var actualExistsValue = Common.existsByAnnotMethodType(value);
            var expectedExistsValue = true;
            assertEquals(expectedExistsValue, actualExistsValue);
        }
    }

    @Test
    void shouldReturnFalseWhenValueIsDefaultValueByAnnotMethodType() {
        Object[] data = {
                false, "", Double.MIN_VALUE, -20, NullType.class, null, new String[] {}
        };
        for (Object value : data) {
            var actualExistsValue = Common.existsByAnnotMethodType(value);
            var expectedExistsValue = false;
            assertEquals(expectedExistsValue, actualExistsValue);
        }
    }

    @Test
    void shouldReturnRightStringByAnnotMethodType() {
        Object[][] data = {
                { Void.class, "Void" },
                { 17.65, "17.65" },
                { -20.5, "-20.5" },
                { 32.0, "32" },
                { 0.0, "0" },
                { 100, "100" },
                { true, "true" }
        };
        for (Object[] value : data) {
            var actualString = Common.toStringByAnnotMethodType(value[0]);
            var expectedString = value[1];
            assertEquals(expectedString, actualString);
        }
    }

    static enum Sample {
        ONE;
    }

}
