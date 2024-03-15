package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectTypeValidatorTest {

    @Test
    void shouldReturnTrueWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { null, Sample.objectType(String.class, false, false, 0) },
                { "text", Sample.objectType(String.class, false, false, 0) },
                { List.of("abc", "def"), Sample.objectType(String.class, true, false, 2) },
                { List.of(Set.of("aa", "bb"), Set.of("cc", "dd")), Sample.objectType(String.class, true, true, 2) }
        };
        for (Object[] value : data) {
            var validator = new ObjectTypeValidator();
            var annotation = (ObjectType) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = true;
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void shouldReturnFalseWhenValidatingObjectAgainstAnnnotation() {
        Object[][] data = {
                { 100, Sample.objectType(String.class, false, false, 0) },
                { List.of(1.1, 2.2), Sample.objectType(String.class, true, false, 2) },
                { Map.of(1.1, 2.2), Sample.objectType(Double.class, true, false, 2) },
                { List.of("a", "b", "c"), Sample.objectType(String.class, true, false, 2) },
                { List.of(List.of(1, 2), List.of(3, 4)), Sample.objectType(String.class, true, true, 2) },
                { List.of(List.of(1, 2), List.of(3, 4)), Sample.objectType(Integer.class, true, true, 1) },
                { List.of(Map.of(1, 2), List.of(3, 4)), Sample.objectType(Integer.class, true, true, 1) }
        };
        for (Object[] value : data) {
            var validator = new ObjectTypeValidator();
            var annotation = (ObjectType) value[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(value[0]);
            var expectedResult = false;
            assertEquals(expectedResult, actualResult);
        }
    }

    static class Sample {

        static ObjectType objectType(Class<?> baseClass, boolean firstGroup, boolean secondGroup, int maxSize) {
            return new ObjectType() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return ObjectType.class;
                }

                @Override
                public String message() {
                    return "";
                }

                @Override
                public Class<?> baseClass() {
                    return baseClass;
                }

                @Override
                public boolean firstGroup() {
                    return firstGroup;
                }

                @Override
                public boolean secondGroup() {
                    return secondGroup;
                }

                @Override
                public int maxSize() {
                    return maxSize;
                }

            };
        }

    }

}
