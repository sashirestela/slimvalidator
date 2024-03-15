package io.github.sashirestela.slimvalidator.data;

import io.github.sashirestela.slimvalidator.Valid;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {

    @Required
    @Size(max = 50)
    String street;

    @Required
    String city;

    @Valid
    Coordinate coordinate;

    @Value
    @Builder
    public static class Coordinate {

        @Required
        @Range(min = -90.0, max = 90.0)
        Double latitude;

        @Required
        @Range(min = -90.0, max = 90.0)
        Double longitude;

    }

}
