package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Checks if an object's type matches a specific schema pattern. Supports direct objects,
 * collections, nested collections, maps, and maps with collection values.
 */
public class ObjectTypeValidator implements ConstraintValidator<ObjectType, Object> {

    private Schema schema;
    private Class<?>[] baseClass;
    private Class<?> keyClass;
    private int maxSize;
    private int maxInnerSize;
    private int maxChecks;
    private boolean allowNull;
    private boolean allowInnerNull;

    @Override
    public void initialize(ObjectType annotation) {
        schema = annotation.schema();
        baseClass = annotation.baseClass().clone();
        keyClass = annotation.keyClass();
        maxSize = annotation.maxSize();
        maxInnerSize = annotation.maxInnerSize();
        maxChecks = annotation.maxChecks();
        allowNull = annotation.allowNull();
        allowInnerNull = annotation.allowInnerNull();
    }

    @Override
    public String getMessage() {
        var plainBaseClass = new StringBuilder();
        for (int i = 0; i < baseClass.length; i++) {
            plainBaseClass.append(baseClass[i].getSimpleName());
            if (i < baseClass.length - 1) {
                plainBaseClass.append("|");
            }
        }
        var message = new StringBuilder();
        switch (schema) {
            case DIRECT:
                message.append(plainBaseClass);
                break;
            case COLL:
                message.append("Collection<").append(plainBaseClass).append(">");
                break;
            case COLL_COLL:
                message.append("Collection<Collection<").append(plainBaseClass).append(">>");
                break;
            case MAP:
                message.append("Map<").append(keyClass.getSimpleName()).append(", ").append(plainBaseClass).append(">");
                break;
            case MAP_COLL:
                message.append("Map<")
                        .append(keyClass.getSimpleName())
                        .append(", Collection<")
                        .append(plainBaseClass)
                        .append(">>");
                break;
        }
        if (maxSize != Integer.MAX_VALUE) {
            message.append(" and size at most ").append(maxSize);
        }
        if (maxInnerSize != Integer.MAX_VALUE) {
            message.append(" and inner size at most ").append(maxInnerSize);
        }
        if (!allowNull) {
            message.append(" and not contain nulls");
        }
        if (!allowInnerNull) {
            message.append(" and not contain inner nulls");
        }
        return message.toString();
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        switch (schema) {
            case DIRECT:
                return isValidDirect(value);
            case COLL:
                return isValidCollection(value);
            case COLL_COLL:
                return isValidCollectionOfCollections(value);
            case MAP:
                return isValidMap(value);
            case MAP_COLL:
                return isValidMapOfCollections(value);
            default:
                return false;
        }
    }

    private boolean isValidDirect(Object value) {
        return isInstanceOfAny(value, baseClass);
    }

    private boolean isValidCollection(Object value) {
        if (!(value instanceof Collection)) {
            return false;
        }
        Collection<?> collection = (Collection<?>) value;
        if (collection.size() > maxSize) {
            return false;
        }
        int checked = 0;
        for (Object item : collection) {
            if (checked >= maxChecks) {
                break;
            }
            if (item == null) {
                if (!allowNull) {
                    return false;
                }
            } else {
                if (!isInstanceOfAny(item, baseClass)) {
                    return false;
                }
            }
            checked++;
        }
        return true;
    }

    private boolean isValidCollectionOfCollections(Object value) {
        if (!(value instanceof Collection)) {
            return false;
        }
        Collection<?> outerCollection = (Collection<?>) value;
        if (outerCollection.size() > maxSize) {
            return false;
        }
        return validateCollectionItems(outerCollection, this::isValidInnerCollection);
    }

    private boolean isValidInnerCollection(Object item) {
        if (item == null) {
            return allowNull;
        }
        if (!(item instanceof Collection)) {
            return false;
        }
        Collection<?> innerCollection = (Collection<?>) item;
        if (innerCollection.size() > maxInnerSize) {
            return false;
        }
        return validateCollectionItems(innerCollection, this::isValidInnerItem);
    }

    private boolean isValidInnerItem(Object item) {
        if (item == null) {
            return allowInnerNull;
        }
        return isInstanceOfAny(item, baseClass);
    }

    private boolean isValidMap(Object value) {
        if (!(value instanceof Map)) {
            return false;
        }
        Map<?, ?> map = (Map<?, ?>) value;

        if (map.size() > maxSize) {
            return false;
        }
        return validateMapEntries(map, this::isValidMapValue);
    }

    private boolean isValidMapValue(Object value) {
        if (value == null) {
            return allowNull;
        }
        return isInstanceOfAny(value, baseClass);
    }

    private boolean isValidMapOfCollections(Object value) {
        if (!(value instanceof Map)) {
            return false;
        }
        Map<?, ?> map = (Map<?, ?>) value;
        if (map.size() > maxSize) {
            return false;
        }
        return validateMapEntries(map, this::isValidMapCollectionValue);
    }

    private boolean isValidMapCollectionValue(Object value) {
        if (value == null) {
            return allowNull;
        }
        if (!(value instanceof Collection)) {
            return false;
        }
        Collection<?> collection = (Collection<?>) value;
        if (collection.size() > maxInnerSize) {
            return false;
        }
        return validateCollectionItems(collection, this::isValidInnerItem);
    }

    private boolean validateCollectionItems(Collection<?> collection, java.util.function.Predicate<Object> validator) {
        int checked = 0;
        for (Object item : collection) {
            if (checked >= maxChecks) {
                break;
            }
            if (!validator.test(item)) {
                return false;
            }
            checked++;
        }
        return true;
    }

    private boolean validateMapEntries(Map<?, ?> map, Predicate<Object> valueValidator) {
        int checked = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (checked >= maxChecks) {
                break;
            }
            Object key = entry.getKey();
            Object val = entry.getValue();
            // Check key type (if keyClass is not void.class)
            if (keyClass != void.class && key != null && !keyClass.isInstance(key)) {
                return false;
            }
            // Check value using provided validator
            if (!valueValidator.test(val)) {
                return false;
            }
            checked++;
        }
        return true;
    }

    private boolean isInstanceOfAny(Object value, Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isInstance(value)) {
                return true;
            }
        }
        return false;
    }

}
