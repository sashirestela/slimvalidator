package io.github.sashirestela.slimvalidator.util;

import java.util.Arrays;

public class Common {

    private Common() {
    }

    public static <T> T[] concatArrays(T[] array1, T[] array2) {
        var result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static boolean isPrimitiveOrWrapper(Object value) {
        var wrapper = Arrays.asList(Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class,
                Double.class, Boolean.class, String.class);
        return (value != null && (value.getClass().isPrimitive() || wrapper.contains(value.getClass())));
    }

}
