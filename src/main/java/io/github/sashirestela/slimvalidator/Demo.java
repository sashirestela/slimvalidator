package io.github.sashirestela.slimvalidator;

import io.github.sashirestela.slimvalidator.constraints.ObjectType;
import io.github.sashirestela.slimvalidator.constraints.Range;
import io.github.sashirestela.slimvalidator.constraints.Required;
import io.github.sashirestela.slimvalidator.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Demo {

    @Data
    @NoArgsConstructor
    public static class Person {

        @Required
        Integer id;

        @Required
        @Size(max = 50)
        String fullName;

        @Required
        Gender gender;

        @Range(min = 2000)
        Double income;

        @Size(min = 3, max = 5)
        String[] hobbies;

        @Valid
        Address address;

        @ObjectType(baseClass = String.class)
        @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 3)
        Object reference;

    }

    @Data
    @NoArgsConstructor
    public static class Address {

        @Required
        @Size(max = 50)
        String streetNumberName;

        @Size(max = 4)
        String apartment;

        @Required
        String city;

    }

    public static enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public static void main(String[] args) {
        /* Instantiate objects of those classes */

        var address = new Address();
        address.setStreetNumberName("1765 Paramount Avenue");
        address.setApartment("123-a");

        var person = new Person();
        person.setFullName("Martin Jefferson");
        person.setGender(Gender.MALE);
        person.setIncome(1850.5);
        person.setHobbies(new String[] { "dancing", "running" });
        person.setAddress(address);
        person.setReference(List.of(10, 20));

        /* Validate objects */

        var validator = new Validator();
        var violations = validator.validate(person);
        violations.forEach(v -> System.out.println(v.getName() + " " + v.getMessage()));

    }

}
