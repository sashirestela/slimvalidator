package io.github.sashirestela.slimvalidator.data;

import io.github.sashirestela.slimvalidator.Valid;
import io.github.sashirestela.slimvalidator.constraints.FieldMatch;
import io.github.sashirestela.slimvalidator.constraints.RequiredIfNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@RequiredIfNull(fields = { "firstName", "lastName" }, dependsOn = "fullName")
@FieldMatch(first = "password", second = "confirmPassword")
public class Participant {

    String firstName;

    String lastName;

    String fullName;

    String password;

    String confirmPassword;

    @Valid
    Participant partner;

}
