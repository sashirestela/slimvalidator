package io.github.sashirestela.slimvalidator.data;

import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class AbstractClass {

    @Required
    protected String name;

    @Range(min = 1, max = 10)
    protected Integer level;

    @Getter
    @SuperBuilder
    public static class ChildClass extends AbstractClass {

        @Required
        private String category;

    }

}
