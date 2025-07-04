package io.github.sashirestela.slimvalidator.validators;

import io.github.sashirestela.slimvalidator.constraints.RequiredIfNull;
import io.github.sashirestela.slimvalidator.data.Participant;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequiredIfNullValidatorTest {

    @Test
    void shouldReturnTrueWhenFieldsAccomplishTheConditions() {
        Participant[] data = {
                Participant.builder().fullName("Jhon Ford").build(),
                Participant.builder().fullName("Jhon Ford").firstName("Jhon").build(),
                Participant.builder().fullName("Jhon Ford").lastName("Ford").build(),
                Participant.builder().fullName("Jhon Ford").firstName("Jhon").lastName("Ford").build()
        };
        var annotation = Sample.requiredIfNull(new String[] { "firstName", "lastName" }, "fullName");
        for (Participant participant : data) {
            var validator = new RequiredIfNullValidator();
            validator.initialize(annotation);
            var result = validator.isValid(participant);
            assertTrue(result);
        }
    }

    @Test
    void shouldReturnFalseWhenFieldsDoNotAccomplishTheConditions() {
        Participant[] data = {
                Participant.builder().build(),
                Participant.builder().firstName("Jhon").build(),
                Participant.builder().lastName("Ford").build()
        };
        var annotation = Sample.requiredIfNull(new String[] { "firstName", "lastName" }, "fullName");
        for (Participant participant : data) {
            var validator = new RequiredIfNullValidator();
            validator.initialize(annotation);
            var result = validator.isValid(participant);
            assertFalse(result);
        }
    }

    static class Sample {

        static RequiredIfNull requiredIfNull(String[] fields, String dependsOn) {
            return new RequiredIfNull() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return RequiredIfNull.class;
                }

                @Override
                public String message() {
                    return "";
                }

                @Override
                public String[] fields() {
                    return fields;
                }

                @Override
                public String dependsOn() {
                    return dependsOn;
                }

            };
        }

    }

}
