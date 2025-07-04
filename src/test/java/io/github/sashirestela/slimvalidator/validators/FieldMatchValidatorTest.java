package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.FieldMatch;
import io.github.sashirestela.slimvalidator.data.Participant;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldMatchValidatorTest {

    @Test
    void shouldReturnTrueWhenFieldsAccomplishTheConditions() {
        Participant[] data = {
                Participant.builder().fullName("Jhon Ford").build(),
                Participant.builder().fullName("Jhon Ford").password("").confirmPassword("").build(),
                Participant.builder().fullName("Jhon Ford").password("qwerty").confirmPassword("qwerty").build()
        };
        var annotation = Sample.fieldMatch("password", "confirmPassword");
        for (Participant participant : data) {
            var validator = new FieldMatchValidator();
            validator.initialize(annotation);
            var result = validator.isValid(participant);
            assertTrue(result);
        }
    }

    @Test
    void shouldReturnFalseWhenFieldsDoNotAccomplishTheConditions() {
        Participant[] data = {
                Participant.builder().fullName("Jhon Ford").password("qwerty").build(),
                Participant.builder().fullName("Jhon Ford").confirmPassword("qwerty").build(),
                Participant.builder().fullName("Jhon Ford").password("qwerty").confirmPassword("asdfgh").build()
        };
        var annotation = Sample.fieldMatch("password", "confirmPassword");
        for (Participant participant : data) {
            var validator = new FieldMatchValidator();
            validator.initialize(annotation);
            var result = validator.isValid(participant);
            assertFalse(result);
        }
    }

    static class Sample {

        static FieldMatch fieldMatch(String first, String second) {
            return new FieldMatch() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return FieldMatch.class;
                }

                @Override
                public String message() {
                    return "";
                }

                @Override
                public String first() {
                    return first;
                }

                @Override
                public String second() {
                    return second;
                }

            };
        }

    }

}
