package io.github.sashirestela.slimvalidator.metadata;

import io.github.sashirestela.slimvalidator.ConstraintValidator;
import lombok.Builder;
import lombok.Value;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class ClassMetadata {

    String fullName;
    List<AnnotationMetadata> annotations;
    List<FieldMetadata> fields;

    @Value
    @Builder
    public static class FieldMetadata {

        String name;
        List<AnnotationMetadata> annotations;

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
