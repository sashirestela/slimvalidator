package io.github.sashirestela.slimvalidator.util;

import io.github.sashirestela.slimvalidator.data.Person;
import io.github.sashirestela.slimvalidator.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectTest {

    @Test
    void shouldReturnFieldValuesFromObjectByFieldName() {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", "Peter");
        data.put("lastName", null);
        data.put("age", 33);
        var person = Person.builder()
                .firstName((String) data.get("firstName"))
                .lastName((String) data.get("lastName"))
                .age((Integer) data.get("age"))
                .build();
        for (var entry : data.entrySet()) {
            var actualValue = Reflect.getValue(person, entry.getKey());
            var expectedValue = entry.getValue();
            assertEquals(expectedValue, actualValue);
        }
    }

    @Test
    void shouldThrownExceptionWhenGetValueWithNonExistingField() {
        var person = Person.builder()
                .firstName("Peter")
                .lastName("Parker")
                .age(33)
                .build();
        assertThrows(ValidationException.class, () -> Reflect.getValue(person, "fullName"));
    }

}
