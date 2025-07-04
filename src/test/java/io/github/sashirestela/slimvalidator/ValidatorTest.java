package io.github.sashirestela.slimvalidator;

import io.github.sashirestela.slimvalidator.data.AbstractClass.ChildClass;
import io.github.sashirestela.slimvalidator.data.Address;
import io.github.sashirestela.slimvalidator.data.Address.Coordinate;
import io.github.sashirestela.slimvalidator.data.Participant;
import io.github.sashirestela.slimvalidator.data.Person;
import io.github.sashirestela.slimvalidator.data.User;
import io.github.sashirestela.slimvalidator.data.User.Gender;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatorTest {

    @Test
    void shouldNotReturnAnyViolationWhenObjectAccomplishAllConstraints() {
        var address1 = Address.builder()
                .street("Indepence")
                .city("Washington")
                .coordinate(Coordinate.builder().latitude(40.5).longitude(-30.5).build())
                .build();
        var address2 = Address.builder()
                .street("Green Valley")
                .city("Colorado")
                .coordinate(Coordinate.builder().latitude(-17.2).longitude(23.8).build())
                .build();
        var user = User.builder()
                .id(101)
                .name("Peter James")
                .email("peter.james@gmail.com")
                .gender(Gender.MALE)
                .active(true)
                .address(address1)
                .address(address2)
                .reference("This is the main reference.")
                .hobbies(new String[] { "dancing", "football", "reading" })
                .relative("wife", Person.builder().firstName("Mary").lastName("Turner").age(25).build())
                .relative("son", Person.builder().firstName("Tom").lastName("James").age(5).build())
                .build();
        var validator = new Validator();
        var violations = validator.validate(user);
        var actualNumberOfViolations = violations.size();
        var expectedNumberOfViolations = 0;
        assertEquals(expectedNumberOfViolations, actualNumberOfViolations);
    }

    @Test
    void shouldReturnViolationsWhenObjectDoesNotAccomplishConstraints() {
        var address1 = Address.builder()
                .street("Indepence")
                .coordinate(Coordinate.builder().longitude(-30.5).build())
                .build();
        var user = User.builder()
                .id(101)
                .name("Peter Thomas Jefferson")
                .address(address1)
                .reference(1001)
                .hobbies(new String[] { "dancing", "football" })
                .relative("wife", Person.builder().firstName("Mary").lastName("Turner").age(0).build())
                .build();
        var validator = new Validator();
        var violations = validator.validate(user);
        var exception = new ConstraintViolationException(violations);
        var actualViolationsMessage = exception.getMessage();
        var expectedViolationMessage = ""
                + "name size must be at most 20.\n"
                + "email must have a value.\n"
                + "gender must have a value.\n"
                + "addresses.0.city must have a value.\n"
                + "addresses.0.coordinate.latitude must have a value.\n"
                + "reference type must be or String or Collection<String> (max 2 items) or Collection<Collection<String>> (max 2 items).\n"
                + "hobbies size must be at least 3.\n"
                + "relatives size must be at least 2.\n"
                + "relatives.wife.age must be at least 1 at most 100.";
        assertEquals(expectedViolationMessage, actualViolationsMessage);
    }

    @Test
    void shouldExecuteValidationWhenThereIsClassHierarchy() {
        var childObject = ChildClass.builder()
                .name("name")
                .level(12)
                .category("category")
                .build();
        var validator = new Validator();
        var violations = validator.validate(childObject);
        var exception = new ConstraintViolationException(violations);
        var actualViolationsMessage = exception.getMessage();
        var expectedViolationMessage = "level must be at least 1 at most 10.";
        assertEquals(expectedViolationMessage, actualViolationsMessage);
    }

    @Test
    void shouldExecuteValidationWhenThereAreDependentFields() {
        var participant = Participant.builder()
                .firstName("George")
                .password("qwerty")
                .partner(Participant.builder()
                        .fullName("Robert Taylor")
                        .partner(Participant.builder()
                                .lastName("Smith")
                                .confirmPassword("qwerty")
                                .build())
                        .build())
                .build();
        var validator = new Validator();
        var violations = validator.validate(participant);
        var exception = new ConstraintViolationException(violations);
        var actualViolationsMessage = exception.getMessage();
        var expectedViolationMessage = "" +
                "[firstName, lastName] must have a value when fullName is null.\n" +
                "password and confirmPassword must match.\n" +
                "in partner.partner [firstName, lastName] must have a value when fullName is null.\n" +
                "in partner.partner password and confirmPassword must match.";
        assertEquals(expectedViolationMessage, actualViolationsMessage);
    }

}
