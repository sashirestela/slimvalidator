package io.github.sashirestela.slimvalidator.util;

import io.github.sashirestela.slimvalidator.exception.ValidationException;

public class Reflect {

    private Reflect() {
    }

    public static Object getValue(Object object, String fieldName) {
        var clazz = object.getClass();
        try {
            var method = clazz.getMethod(getMethodName(fieldName), (Class<?>[]) null);
            return method.invoke(object);
        } catch (Exception e) {
            throw new ValidationException("Cannot read the field {0}.{1}().", clazz.getSimpleName(), fieldName, e);
        }
    }

    private static String getMethodName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
