package io.github.sashirestela.slimvalidator.data;

import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.ObjectType.Schema;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class User {

    @Required
    Integer id;

    @Required
    @Size(max = 20)
    String name;

    @Required
    @Size(max = 40)
    String email;

    @Required
    Gender gender;

    Boolean active;

    @Singular
    @Size(min = 1, max = 3)
    List<Address> addresses;

    @ObjectType(baseClass = String.class)
    @ObjectType(schema = Schema.COLL, baseClass = String.class, maxSize = 2)
    @ObjectType(schema = Schema.MAP, keyClass = String.class, baseClass = String.class, maxSize = 2)
    Object reference;

    @Size(min = 3)
    String[] hobbies;

    @Singular
    @Size(min = 2)
    Map<String, Person> relatives;

    public static enum Gender {
        MALE,
        FEMALE,
        OTHER;
    }

}
