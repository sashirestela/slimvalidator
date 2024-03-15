package io.github.sashirestela.slimvalidator;

import io.github.sashirestela.slimvalidator.exception.ValidationException;
import io.github.sashirestela.slimvalidator.metadata.ClassMetadata.AnnotationMetadata;
import io.github.sashirestela.slimvalidator.metadata.ClassMetadata.FieldMetadata;
import io.github.sashirestela.slimvalidator.metadata.MetadataStore;
import io.github.sashirestela.slimvalidator.util.Common;
import io.github.sashirestela.slimvalidator.util.Node;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main class to execute validation process.
 */
public class Validator {

    /**
     * Check if all the object's fields accomplish their constraints.
     * 
     * @param object Whose fields will be validated.
     * @return List of all the unaccomplished constraints.
     */
    public <T> List<ConstraintViolation> validate(T object) {
        var context = new ValidationContext();
        this.validateObject(object, context, new Node());
        return context.getViolations();
    }

    private void validateObject(Object object, ValidationContext context, Node node) {
        if (Common.isPrimitiveOrWrapper(object)) {
            return;
        }
        context.visit(object);
        var clazz = object.getClass();
        var classMetadata = MetadataStore.one().get(clazz);
        for (var fieldMetadata : classMetadata.getFields()) {
            this.validateField(fieldMetadata, object, context, node);
        }
    }

    private void validateField(FieldMetadata fieldMetadata, Object object, ValidationContext context, Node node) {
        var fieldValue = fieldMetadata.getValue(object);
        var fieldName = fieldMetadata.getName();
        for (var annotationMetadata : fieldMetadata.getAnnotations()) {
            var annotation = annotationMetadata.getAnnotation();
            var violationOptional = this.validateAnnotation(annotationMetadata, annotation, fieldValue,
                    node.child(fieldName).toString());
            if (violationOptional.isPresent()) {
                context.addViolation(violationOptional.get());
            }
        }
        if (context.isVisited(fieldValue) || fieldValue == null) {
            return;
        }
        if (fieldValue instanceof Collection) {
            var i = 0;
            for (var item : (Collection<?>) fieldValue) {
                this.validateObject(item, context, node.child(fieldName).child((String.valueOf(i++))));
            }
        } else if (fieldValue instanceof Map) {
            for (var entry : ((Map<?, ?>) fieldValue).entrySet()) {
                this.validateObject(entry.getValue(), context, node.child(fieldName).child(entry.getKey().toString()));
            }
        } else if (fieldValue.getClass().isArray()) {
            var i = 0;
            for (var item : (Object[]) fieldValue) {
                this.validateObject(item, context, node.child(fieldName).child((String.valueOf(i++))));
            }
        } else {
            this.validateObject(fieldValue, context, node.child(fieldName));
        }
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation, T> Optional<ConstraintViolation> validateAnnotation(
            AnnotationMetadata annotationMetadata, A annotation, T fieldValue, String pathName) {
        var constraintValidatorClass = annotationMetadata.getValidatedBy();
        if (constraintValidatorClass == null) {
            return Optional.empty();
        }
        try {
            var constraintValidator = constraintValidatorClass
                    .getConstructor()
                    .newInstance();
            constraintValidator.initialize(annotation);
            if (constraintValidator.isValid(fieldValue)) {
                return Optional.empty();
            } else {
                return Optional.of(new ConstraintViolation(fieldValue, pathName, annotationMetadata));
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new ValidationException("Cannot instantiate the class {0}.", constraintValidatorClass.getSimpleName(),
                    e);
        }
    }

}
