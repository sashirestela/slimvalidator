package io.github.sashirestela.slimvalidator.data;

import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Person {

    @Required
    String firstName;

    @Required
    String lastName;

    @Range(min = 1, max = 100)
    Integer age;

}
