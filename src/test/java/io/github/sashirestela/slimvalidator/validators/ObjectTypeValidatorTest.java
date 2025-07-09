package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectTypeValidatorTest {

    private void assertData(Object[][] data, boolean expectedResult) {
        for (Object[] testCase : data) {
            var validator = new ObjectTypeValidator();
            var annotation = (ObjectType) testCase[1];
            validator.initialize(annotation);
            var actualResult = validator.isValid(testCase[0]);
            if (expectedResult) {
                assertTrue(actualResult, "Expected true for value: " + testCase[0]);
            } else {
                assertFalse(actualResult, "Expected false for value: " + testCase[0]);
            }
        }
    }

    @Test
    void shouldReturnTrueWhenValidatingDirectObjects() {
        Object[][] data = {
                { null, Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class }) },
                { "text", Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class }) },
                { 42, Sample.objectType(Schema.DIRECT, new Class<?>[] { Integer.class }) },
                { 3.14, Sample.objectType(Schema.DIRECT, new Class<?>[] { Double.class }) },
                { "text", Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class, Integer.class }) },
                { 42, Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class, Integer.class }) }
        };
        assertData(data, true);
    }

    @Test
    void shouldReturnFalseWhenValidatingDirectObjects() {
        Object[][] data = {
                { 100, Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class }) },
                { "text", Sample.objectType(Schema.DIRECT, new Class<?>[] { Integer.class }) },
                { 3.14, Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class, Integer.class }) }
        };
        assertData(data, false);
    }

    @Test
    void shouldReturnTrueWhenValidatingCollections() {
        Object[][] data = {
                { null, Sample.objectType(Schema.COLL, new Class<?>[] { String.class }) },
                { List.of("abc", "def"), Sample.objectType(Schema.COLL, new Class<?>[] { String.class }) },
                { Arrays.asList("a", null, "b"),
                        Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, true, true) },
                { List.of(1, 2, 3), Sample.objectType(Schema.COLL, new Class<?>[] { Integer.class }) },
                { new ArrayList<String>(), Sample.objectType(Schema.COLL, new Class<?>[] { String.class }) },
                { List.of("a", "b"), Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, 2) }
        };
        assertData(data, true);
    }

    @Test
    void shouldReturnFalseWhenValidatingCollections() {
        Object[][] data = {
                { "not a collection", Sample.objectType(Schema.COLL, new Class<?>[] { String.class }) },
                { List.of(1, 2), Sample.objectType(Schema.COLL, new Class<?>[] { String.class }) },
                { List.of("a", "b", "c"), Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, 2) },
                { Arrays.asList("a", null, "b"),
                        Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, false, true) },
                { List.of("a", "b"), Sample.objectType(Schema.COLL, new Class<?>[] { Integer.class }) }
        };
        assertData(data, false);
    }

    @Test
    void shouldReturnTrueWhenValidatingCollectionOfCollections() {
        Object[][] data = {
                { null, Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { List.of(List.of("a", "b"), List.of("c", "d")),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { List.of(Set.of("aa", "bb"), Set.of("cc", "dd")),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { Arrays.asList(Arrays.asList("a", null), null),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }, true, true) },
                { List.of(new ArrayList<String>(), List.of("x")),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) }
        };
        assertData(data, true);
    }

    @Test
    void shouldReturnFalseWhenValidatingCollectionOfCollections() {
        Object[][] data = {
                { "not a collection", Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { List.of("a", "b"), Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { List.of(List.of(1, 2), List.of(3, 4)),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }) },
                { List.of(List.of("a", "b"), List.of("c", "d")),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }, 1) },
                { Arrays.asList(Arrays.asList("a", null), null),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }, false, true) },
                { List.of(List.of("a", "b"), List.of("c", "d", "e")),
                        Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }, true, true, 2) }
        };
        assertData(data, false);
    }

    @Test
    void shouldReturnTrueWhenValidatingMaps() {
        Object[][] data = {
                { null, Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", "value1", "key2", "value2"),
                        Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of(1, "value1", 2, "value2"),
                        Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, Integer.class) },
                { new HashMap<String, String>(),
                        Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of("key", "value"),
                        Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class, 1) }
        };
        assertData(data, true);
    }

    @Test
    void shouldReturnFalseWhenValidatingMaps() {
        Object[][] data = {
                { "not a map", Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", 123), Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of(1, "value"), Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", "value1", "key2", "value2"),
                        Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, String.class, 1) }
        };
        assertData(data, false);
    }

    @Test
    void shouldReturnTrueWhenValidatingMapOfCollections() {
        Object[][] data = {
                { null, Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", List.of("a", "b"), "key2", List.of("c", "d")),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) },
                { Map.of(1, Set.of("x", "y")),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, Integer.class) },
                { Map.of("key", new ArrayList<String>()),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) }
        };
        assertData(data, true);
    }

    @Test
    void shouldReturnFalseWhenValidatingMapOfCollections() {
        Object[][] data = {
                { "not a map", Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", "not a collection"),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) },
                { Map.of("key1", List.of(123)),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) },
                { Map.of(1, List.of("a")),
                        Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, String.class) }
        };
        assertData(data, false);
    }

    @Test
    void shouldGenerateCorrectMessages() {
        var validator = new ObjectTypeValidator();

        // Test direct message
        validator.initialize(Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class }));
        assertEquals("String", validator.getMessage());

        // Test collection message
        validator.initialize(Sample.objectType(Schema.COLL, new Class<?>[] { String.class }));
        assertEquals("Collection<String>", validator.getMessage());

        // Test collection of collections message
        validator.initialize(Sample.objectType(Schema.COLL_COLL, new Class<?>[] { String.class }));
        assertEquals("Collection<Collection<String>>", validator.getMessage());

        // Test map message
        validator.initialize(Sample.objectType(Schema.MAP, new Class<?>[] { String.class }, Integer.class));
        assertEquals("Map<Integer, String>", validator.getMessage());

        // Test map of collections message
        validator.initialize(Sample.objectType(Schema.MAP_COLL, new Class<?>[] { String.class }, Integer.class));
        assertEquals("Map<Integer, Collection<String>>", validator.getMessage());

        // Test multiple base classes
        validator.initialize(Sample.objectType(Schema.DIRECT, new Class<?>[] { String.class, Integer.class }));
        assertEquals("String|Integer", validator.getMessage());

        // Test with size constraints
        validator.initialize(Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, 10));
        assertEquals("Collection<String> and size at most 10", validator.getMessage());

        // Test with null constraints
        validator.initialize(Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, false, true));
        assertEquals("Collection<String> and not contain nulls", validator.getMessage());
    }

    @Test
    void shouldHandleMaxChecksLimit() {
        var validator = new ObjectTypeValidator();
        var annotation = Sample.objectType(Schema.COLL, new Class<?>[] { String.class }, void.class, Integer.MAX_VALUE,
                Integer.MAX_VALUE, 1, true, true); // maxChecks = 1
        validator.initialize(annotation);

        // Create a list with many elements, but only first one will be checked
        var largeList = new ArrayList<Object>();
        largeList.add("valid");
        for (int i = 0; i < 100; i++) {
            largeList.add(123); // Invalid type, but should not be checked due to maxChecks limit
        }

        assertTrue(validator.isValid(largeList));
    }

    static class Sample {

        static ObjectType objectType(Schema schema, Class<?>[] baseClass) {
            return objectType(schema, baseClass, void.class, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, true, true);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, int maxSize) {
            return objectType(schema, baseClass, void.class, maxSize, Integer.MAX_VALUE, 20, true, true);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, boolean allowNull, boolean allowInnerNull) {
            return objectType(schema, baseClass, void.class, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, allowNull,
                    allowInnerNull);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, boolean allowNull, boolean allowInnerNull,
                int maxInnerSize) {
            return objectType(schema, baseClass, void.class, Integer.MAX_VALUE, maxInnerSize, 20, allowNull,
                    allowInnerNull);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, Class<?> keyClass) {
            return objectType(schema, baseClass, keyClass, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, true, true);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, Class<?> keyClass, int maxSize) {
            return objectType(schema, baseClass, keyClass, maxSize, Integer.MAX_VALUE, 20, true, true);
        }

        static ObjectType objectType(Schema schema, Class<?>[] baseClass, Class<?> keyClass, int maxSize,
                int maxInnerSize, int maxChecks, boolean allowNull, boolean allowInnerNull) {
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
                public Schema schema() {
                    return schema;
                }

                @Override
                public Class<?>[] baseClass() {
                    return baseClass.clone();
                }

                @Override
                public Class<?> keyClass() {
                    return keyClass;
                }

                @Override
                public int maxSize() {
                    return maxSize;
                }

                @Override
                public int maxInnerSize() {
                    return maxInnerSize;
                }

                @Override
                public int maxChecks() {
                    return maxChecks;
                }

                @Override
                public boolean allowNull() {
                    return allowNull;
                }

                @Override
                public boolean allowInnerNull() {
                    return allowInnerNull;
                }

            };
        }

    }

}
