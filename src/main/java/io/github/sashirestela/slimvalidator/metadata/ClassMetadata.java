package io.github.sashirestela.slimvalidator.metadata;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import lombok.Builder;
import lombok.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class ClassMetadata {

    String fullName;
    List<FieldMetadata> fields;

    @Value
    @Builder
    public static class FieldMetadata {

        String name;
        List<AnnotationMetadata> annotations;

        public Object getValue(Object object) {
            var clazz = object.getClass();
            try {
                var method = clazz.getDeclaredMethod(getMethodName(name), (Class<?>[]) null);
                return method.invoke(object);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new ValidationException("Cannot read the field {0}.{1}().", clazz.getSimpleName(), name, e);
            }
        }

        private String getMethodName(String name) {
            return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }

    }

    @Value
    @Builder
    public static class AnnotationMetadata {

        @SuppressWarnings("rawtypes")
        Class<? extends ConstraintValidator> validatedBy;
        Map<String, Object> valuesByAnnotMethod;
        List<AnnotationMetadata> subAnnotations;
        Annotation annotation;

    }

}
