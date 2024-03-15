package io.github.sashirestela.slimvalidator.util;

import javax.lang.model.type.NullType;

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

    public static boolean existsByAnnotMethodType(Object value) {

        if (Boolean.class.isInstance(value)) {
            return (Boolean) value;
        }
        if (String.class.isInstance(value)) {
            return !((String) value).isEmpty();
        }
        if (Double.class.isInstance(value)) { // it is used by Range annotation.
            var doubleValue = (Double) value;
            return (doubleValue > Double.MIN_VALUE && doubleValue < Double.MAX_VALUE);
        }
        if (Integer.class.isInstance(value)) { // it is used by Size annotation.
            var integerValue = (Integer) value;
            return (integerValue > 0 && integerValue < Integer.MAX_VALUE);
        }
        if (Class.class.isInstance(value)) {
            return !((Class<?>) value).equals(NullType.class);
        }
        if (value != null && value.getClass().isArray()) {
            return ((Object[]) value).length > 0;
        }
        return value != null;
    }

    public static String toStringByAnnotMethodType(Object value) {
        if (Class.class.isInstance(value)) {
            return ((Class<?>) value).getSimpleName();
        }
        if (Double.class.isInstance(value)) {
            if (((Double) value) % 1 == 0) {
                return String.valueOf(((Double) value).intValue());
            } else {
                return value.toString();
            }
        }
        return value.toString();
    }

}
