package io.github.sashirestela.slimvalidator.metadata;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.Valid;
import io.github.sashirestela.slimvalidator.metadata.ClassMetadata.AnnotationMetadata;
import io.github.sashirestela.slimvalidator.metadata.ClassMetadata.FieldMetadata;
import io.github.sashirestela.slimvalidator.util.Common;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MetadataStore {

    private static MetadataStore store = null;

    private Map<String, ClassMetadata> classesByFullName;

    private MetadataStore() {
        classesByFullName = new ConcurrentHashMap<>();
    }

    public static MetadataStore one() {
        if (store == null) {
            store = new MetadataStore();
        }
        return store;
    }

    public ClassMetadata get(Class<?> clazz) {
        var className = clazz.getName();
        if (!classesByFullName.containsKey(className)) {
            save(clazz);
        }
        return classesByFullName.get(className);
    }

    private void save(Class<?> clazz) {
        List<FieldMetadata> fields = new ArrayList<>();
        for (var javaField : getFullFields(clazz)) {
            List<AnnotationMetadata> annotations = getContraintAnnotations(javaField.getDeclaredAnnotations());
            if (!annotations.isEmpty()) {
                var fieldMetadata = FieldMetadata.builder()
                        .name(javaField.getName())
                        .annotations(annotations)
                        .build();
                fields.add(fieldMetadata);
            }
        }
        List<AnnotationMetadata> annotations = getContraintAnnotations(clazz.getDeclaredAnnotations());
        var classMetadata = ClassMetadata.builder()
                .fullName(clazz.getName())
                .annotations(annotations)
                .fields(fields)
                .build();
        classesByFullName.put(clazz.getName(), classMetadata);
        log.debug("The class {} was saved", clazz.getSimpleName());
    }

    private Field[] getFullFields(Class<?> clazz) {
        var fullFields = clazz.getDeclaredFields();
        var superClazz = clazz.getSuperclass();
        while (superClazz != null) {
            fullFields = Common.concatArrays(superClazz.getDeclaredFields(), fullFields);
            superClazz = superClazz.getSuperclass();
        }
        return fullFields;
    }

    private List<AnnotationMetadata> getContraintAnnotations(Annotation[] javaAnnotations) {
        List<AnnotationMetadata> annotations = new ArrayList<>();
        for (var javaAnnotation : javaAnnotations) {
            var javaAnnotationType = javaAnnotation.annotationType();
            if (!javaAnnotationType.isAnnotationPresent(Constraint.class)
                    && !(javaAnnotation instanceof Valid)) {
                continue;
            }
            Map<String, Object> valuesByAnnotMethod = new ConcurrentHashMap<>();
            List<AnnotationMetadata> subAnnotations = null;
            for (var javaAnnotMethod : javaAnnotationType.getDeclaredMethods()) {
                Object value;
                try {
                    value = javaAnnotMethod.invoke(javaAnnotation, (Object[]) null);
                } catch (Exception e) {
                    value = null;
                }
                if (value instanceof Annotation[]) {
                    subAnnotations = getContraintAnnotations((Annotation[]) value);
                    value = new Annotation[0];
                }
                valuesByAnnotMethod.put(javaAnnotMethod.getName(), value);
            }
            var constraintClass = javaAnnotationType.getAnnotation(Constraint.class);
            var validatedBy = constraintClass != null ? constraintClass.validatedBy() : null;
            var annotationMetadata = AnnotationMetadata.builder()
                    .validatedBy(validatedBy)
                    .valuesByAnnotMethod(valuesByAnnotMethod)
                    .subAnnotations(Optional.ofNullable(subAnnotations).orElse(new ArrayList<>()))
                    .annotation(javaAnnotation)
                    .build();
            annotations.add(annotationMetadata);
        }
        return annotations;
    }

}
