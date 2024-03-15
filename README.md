# 🛡 SlimValidator

Java lightweight validator.

[![codecov](https://codecov.io/gh/sashirestela/slimvalidator/graph/badge.svg?token=IGN9Z5LOOI)](https://codecov.io/gh/sashirestela/slimvalidator)
![Maven Central](https://img.shields.io/maven-central/v/io.github.sashirestela/slimvalidator)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/sashirestela/slimvalidator/build_java_maven.yml)

## 💡 Description
SlimValidator is a Java library for providing object validation through annotations. It is inspired by the Java Bean Validation specification but is not a implementation at all.

For example, for validating a class's object, we need to annotate their fields with constraints and then we use the class `Validator` to evaluate all the constraints:

```java
/* Classes definition with constraint annotations */

class Person {

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

  // Constructors , getters, setters, etc.

}

class Address {

  @Required
  @Size(max = 50)
  String streetNumberName;

  @Size(max = 4)
  String apartment;

  @Required
  String city;

  // Constructors , getters, setters, etc.

}

enum Gender {
  MALE,
  FEMALE,
  OTHER
}

/* Instantiate objects of those classes */

var address = new Address();
address.setStreetNumberName("1765 Paramount Avenue");
address.setApartment("123-A");

var person = new Person();
person.setFullName("Martin Jefferson");
person.setGender(Gender.MALE);
person.setIncome(1850.5);
person.setHobbies(new String[] {"dancing", "running"});
person.setAddress(address);
person.setReference(List.of(10, 20));

/* Validate objects */

var validator = new Validator();
var violations = validator.validate(person);
violations.forEach(v -> System.out.println(v.getName() + " " + v.getMessage()));
```
As a result of the validation process, you will see the following messages in console:
```txt
id must not be null.
income must be at least 2000.
hobbies size must be at least 3 at most 5.
address.apartment size must be at most 4.
address.city must not be null.
reference type must be or String or Collection<String> (max 3 items).
```

## 🚩 Field Constraints

### @Required
- **Description**: Checks that a value is not null.
- **Applies to**: Fields of any specific type.
- **Parameters**:
    - _(none)_.
- **Error messages**:
    - If the value is null:
        - must not be null.
- **Example**:
    ```java
    @Required
    private Long id;
    ```

### @Range
- **Description**: Checks that a value is within a closed range.
- **Applies to**: Fields of any numeric type.
- **Parameters**:
    - _min_: The lowest value of the range. By default is Double.MIN_VALUE.
    - _max_: The greatest value of the range. By default is Double.MAX_VALUE.
- **Error messages**:
    - If _min_ was set and the value is lower:
        - must be at least {min}.
    - If _max_ was set and the value is greater:
        - must be at most {max}.
    - If _min_ and _max_ were set and the value is out of range:
        - must be at least {min} at most {max}.
- **Example**:
    ```java
    @Range(min = 0.0, max = 100.0)
    private Double grade;
    ```

### @Size
- **Description**: Checks that a text's length or a group's size is within a closed range.
- **Applies to**: Fields of type: String, Collection, Map or Object array.
- **Parameters**:
    - _min_: The lowest value of the length or size. By default is 0.
    - _max_: The greatest value of the length or size. By default is Integer.MAX_VALUE.
- **Error messages**:
    - If _min_ was set and the length or size is lower:
        - size must be at least {min}.
    - If _max_ was set and the length or size is greater:
        - size must be at most {max}.
    - If _min_ and _max_ were set and the length or size is out of range:
        - size must be at least {min} at most {max}.
- **Example**:
    ```java
    @Size(min = 2, max = 5)
    private List<Project> projects;
    ```

### @ObjectType
- **Description**: Checks that the type of an object is one of a list of candidate types.
- **Applies to**: Fields of Object type.
- **Parameters**:
    - _baseClass_: A candidate base class for the field. Mandatory.
    - _firstGroup_: A boolean to indicate if the type is a Collection of the base class. By default is false.
    - _secondGroup_: A boolean to indicate if the type is a Collection of Collection of the base class. By default is false.
    - _maxSize_: The greatest size of the first Collection if set. By default is 0.
- **Error messages for each @ObjectType**:
    - If only _baseClass_ was set and the field type does not match:
        - {baseClass}
    - If _firstGroup_ was set and the field is not Collection of the baseClass:
        - Collection<{baseClass}>
    - If _firstGroup_ and _maxSize_ were set and the field is not a Collection of the baseClass or collection size is greater than maxSize:
        - Collection<{baseClass}> (max {maxSize} items)
    - If _firstGroup_ and _secondGroup_ were set and the field is not a Collection of Collection of the baseClass:
        - Collection<Collection<{baseClass}>>
    - If _firstGroup_, _secondGroup_ and _maxSize_ were set aand the field is not a Collection of Collection of the baseClass or first collection size is greater than maxSize:
        - Collection<Collection<{baseClass}>> (max {maxSize} items)
- **Error message for all @ObjectType**:
    - If the field type does not match any ObjectType:
        - type must be or {msg for ObjectType 1} or {msg for ObjectType 2} ... or {msg for ObjectType N}
- **Example**:
    ```java
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 2)
    @ObjectType(baseClass = String.class, firstGroup = true, secondGroup = true, maxSize = 2)
    Object reference;
    ```

### @Valid
- **Description**: Flag to do nested validation for fields without any constraint.
- **Applies to**: Fields of custom classes that do not have any constraint but it requires to validate their nested fields. Any constraint (Required, Range, Size, ObjectType) enable nested validation automatically.
- **Parameters**:
    - _(none)_.
- **Error messages**:
    - _(none)_.
- **Example**:
    ```java
    @Valid
    private Address mainAddress;
    ```

## 🛠️ Installation

You can install this library by adding the following dependency to your Maven project:

```xml
<dependency>
    <groupId>io.github.sashirestela</groupId>
    <artifactId>slimvalidator</artifactId>
    <version>[latest version]</version>
</dependency>
```

Or alternatively using Gradle:

```groovy
dependencies {
    implementation 'io.github.sashirestela:slimvalidator:[latest version]'
}
```

NOTE: Requires Java 11 or greater.

## 💼 Contributing
Please read our [Contributing](CONTRIBUTING.md) guide to learn and understand how to contribute to this project.


## 📄 License

This library is licensed under the MIT License. See the [LICENSE](https://github.com/sashirestela/slimvalidator/blob/main/LICENSE) file for more information.