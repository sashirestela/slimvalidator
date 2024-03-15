package io.github.sashirestela.slimvalidator.constraints;

import io.github.sashirestela.slimvalidator.Constraint;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.List;
import io.github.sashirestela.slimvalidator.validators.ObjectTypeListValidator;
import io.github.sashirestela.slimvalidator.validators.ObjectTypeValidator;

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
@Repeatable(List.class)
public @interface ObjectType {

    String message() default ""
            + "#if(firstGroup)Collection\\<#endif"
            + "#if(secondGroup)Collection\\<#endif"
            + "{baseClass}"
            + "#if(secondGroup)>#endif"
            + "#if(firstGroup)>#endif"
            + "#if(maxSize) (max {maxSize} items)#endif";

    Class<?> baseClass();

    boolean firstGroup() default false;

    boolean secondGroup() default false;

    int maxSize() default 0;

    @Documented
    @Constraint(validatedBy = ObjectTypeListValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        String message() default "type must be#for(value) or {message}#endfor.";

        ObjectType[] value();

    }

}
