package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.ObjectTypes;
import io.github.sashirestela.slimvalidator.validators.ObjectTypeValidator;
import io.github.sashirestela.slimvalidator.validators.ObjectTypesValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ObjectTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ObjectTypes.class)
public @interface ObjectType {

    enum Schema {
        DIRECT,      // baseClass
        COLL,        // Collection<baseClass>
        COLL_COLL,   // Collection<Collection<baseClass>>
        MAP,         // Map<keyClass, baseClass>
        MAP_COLL     // Map<keyClass, Collection<baseClass>>
    }

    Schema schema() default Schema.DIRECT;

    Class<?>[] baseClass();

    Class<?> keyClass() default void.class;

    int maxSize() default Integer.MAX_VALUE;

    int maxInnerSize() default Integer.MAX_VALUE;

    int maxChecks() default 20;

    boolean allowNull() default true;

    boolean allowInnerNull() default true;

    @Documented
    @Constraint(validatedBy = ObjectTypesValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @interface ObjectTypes {

        ObjectType[] value();

    }

}
