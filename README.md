# 🛡 SlimValidator

Java lightweight validator.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sashirestela_slimvalidator&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sashirestela_slimvalidator)
[![codecov](https://codecov.io/gh/sashirestela/slimvalidator/graph/badge.svg?token=IGN9Z5LOOI)](https://codecov.io/gh/sashirestela/slimvalidator)
![Maven Central](https://img.shields.io/maven-central/v/io.github.sashirestela/slimvalidator)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/sashirestela/slimvalidator/build_java_maven.yml)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/sashirestela/slimvalidator)

### Table of Contents
- [Description](#-description)
- [Installation](#-installation)
- [Field-Level Constraints](#-field-level-constraints)
  - [@Required](#required)
  - [@Range](#range)
  - [@Size](#size)
  - [@Extension](#extension)
  - [@ObjectType](#objecttype)
  - [@Valid](#valid)
- [Class-Level Constraints](#-class-level-constraints)
  - [@RequiredIfNull](#requiredifnull)
  - [@FieldMatch](#fieldmatch)
- [Create New Constraint](#-create-new-constraint)
  - [New Constraint Annotation](#new-constraint-annotation)
  - [New Validator Class](#new-validator-class)
- [Contributing](#-contributing)
- [License](#-license)


## 💡 Description
SlimValidator is a Java library for providing object validation through annotations. It is inspired by the Java Bean Validation specification but is not a implementation at all.

For example, to validate the object of a class, we need to annotate its class or fields with constraints and then use the `Validator` class to evaluate whether the object meets all the constraints:

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

  @Extension({"jpg", "png", "bmp"})
  Path photograph;

  @Valid
  User user;

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

@RequiredIfNull(fields = {"userName"}, dependsOn = "email")
@FieldMatch(first = "password", second = "confirmPassword")
class User {
  
  String username;

  String email;

  @Required
  String password;

  @Required
  String confirmPassword;

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

var user = new User();
user.setPassword("q1w2e3");
user.setConfirmPassword("q1w2e3");

var person = new Person();
person.setFullName("Martin Jefferson");
person.setGender(Gender.MALE);
person.setIncome(1850.5);
person.setHobbies(new String[] {"dancing", "running"});
person.setAddress(address);
person.setReference(List.of(10, 20));
person.setPhotograph(Paths.get("src/test/resources/sample.txt"));

/* Validate objects */

var validator = new Validator();
var violations = validator.validate(person);
if (violations.size() > 0) {
  violations.forEach(v -> System.out.println(v.getName() + " " + v.getMessage()));
}
```
As a result of the validation process, you will see the following messages in console, because the object does not meet several constraints:
```txt
id must have a value.
income must be at least 2000.
hobbies size must be at least 3 at most 5.
address.apartment size must be at most 4.
address.city must have a value.
reference type must be or String or Collection<String> (max 3 items).
photograph extension must be one of [jpg, png, bmp].
in user [username] must have a value when email is null.
```

## ⚙ Installation

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

## 🎯 Field-Level Constraints

### @Required
- **Description**: Checks that a value is not null. In case the value is a group (Collection, Map, Array) checks that it is not empty.
- **Applies to**: Fields of any type.
- **Parameters**:
    - _(none)_.
- **Error messages**:
    - If the value is null or an empty group:
        - _must have a value._
- **Examples**:
    ```java
    @Required
    private Long id;

    @Required
    private List<Adress> addresses;
    ```

### @Range
- **Description**: Checks that a value is within a closed range.
- **Applies to**: Fields of any numeric type.
- **Parameters**:
    - _min_: The lowest value of the range. By default is Double.MIN_VALUE.
    - _max_: The greatest value of the range. By default is Double.MAX_VALUE.
- **Error messages**:
    - If _min_ was set and the value is lower:
        - _must be at least {min}._
    - If _max_ was set and the value is greater:
        - _must be at most {max}._
    - If _min_ and _max_ were set and the value is out of range:
        - _must be at least {min} at most {max}._
- **Example**:
    ```java
    @Range(min = 0.0, max = 100.0)
    private Double grade;
    ```

### @Size
- **Description**: Checks that a text's length or a group's size is within a closed range.
- **Applies to**: Fields of type: String, Collection, Map, Array.
- **Parameters**:
    - _min_: The lowest value of the length or size. By default is 0.
    - _max_: The greatest value of the length or size. By default is Integer.MAX_VALUE.
- **Error messages**:
    - If _min_ was set and the length or size is lower:
        - _size must be at least {min}._
    - If _max_ was set and the length or size is greater:
        - _size must be at most {max}._
    - If _min_ and _max_ were set and the length or size is out of range:
        - _size must be at least {min} at most {max}._
- **Example**:
    ```java
    @Size(min = 2, max = 5)
    private List<Project> projects;
    ```

### @Extension
- **Description**: Checks that the file extension is one of an expected list.
- **Applies to**: Fields of type: java.nio.file.Path, java.io.File.
- **Parameters**:
    - _value_: Array of expected extensions. Mandatory.
- **Error messages**:
    - If file extension is not any of the _value_ array:
        - _extension must be one of {value}._
- **Example**:
    ```java
    @Extension({"doc", "xls", "txt"})
    private Path evidenceFile;
    ```

### @ObjectType
- **Description**: Checks that the type of an object is one of a list of candidate types.
- **Applies to**: Fields of the Object type, including Collection of objects or Collection of Collection of objects. Collection can be any subinterface such as: List, Set, etc.
- **Parameters**:
    - _baseClass_: A candidate base class for the field. Mandatory.
    - _firstGroup_: A boolean to indicate if the type is a Collection of the base class. By default is false.
    - _secondGroup_: A boolean to indicate if the type is a Collection of Collection of the base class. By default is false.
    - _maxSize_: The greatest size of the first Collection if set. By default is 0.
- **Error messages for each @ObjectType**:
    - If only _baseClass_ was set and the field type does not match:
        - _{baseClass}_
    - If _firstGroup_ was set and the field is not Collection of the baseClass:
        - _Collection<{baseClass}>_
    - If _firstGroup_ and _maxSize_ were set and the field is not a Collection of the baseClass or collection size is greater than maxSize:
        - _Collection<{baseClass}> (max {maxSize} items)_
    - If _firstGroup_ and _secondGroup_ were set and the field is not a Collection of Collection of the baseClass:
        - _Collection<Collection<{baseClass}>>_
    - If _firstGroup_, _secondGroup_ and _maxSize_ were set aand the field is not a Collection of Collection of the baseClass or first collection size is greater than maxSize:
        - _Collection<Collection<{baseClass}>> (max {maxSize} items)_
- **Error message for all @ObjectType**:
    - If the field type does not match any ObjectType:
        - _type must be or {msg for ObjectType 1} or {msg for ObjectType 2} ... or {msg for ObjectType N}._
- **Example**:
    ```java
    @ObjectType(baseClass = String.class)
    @ObjectType(baseClass = String.class, firstGroup = true, maxSize = 2)
    @ObjectType(baseClass = String.class, firstGroup = true, secondGroup = true, maxSize = 2)
    Object reference;
    ```

### @Valid
- **Description**: Flag to do nested validation for fields without any constraint.
- **Applies to**: Fields of custom classes that do not have any constraint but it requires to validate their nested fields. Any filed-level constraint enable nested validation automatically.
- **Parameters**:
    - _(none)_.
- **Error messages**:
    - _(none)_.
- **Example**:
    ```java
    @Valid
    private Address mainAddress;
    ```

## 📦 Class-Level Constraints

### @RequiredIfNull
- **Description**: Checks that all fields in a list are not null when the dependsOn field is null.
- **Applies to**: Fields of any type.
- **Parameters**:
    - _fields_: Array of field names to evaluate. Mandatory.
    - _dependsOn_: Name of the reference field. Mandatory.
- **Error messages**:
    - If any of the _fields_ is null when the _dependsOn_ field is null:
        - _{fields} must have a value when {dependsOn} is null._
- **Examples**:
    ```java
    @RequiredIfNull(fields = {"firstName", "lastName"}, dependsOn = "fullName")
    class User {
        private String firstName;
        private String lastName;
        private String fullName;
    }
    ```

### @FieldMatch
- **Description**: Checks that two fields match.
- **Applies to**: Fields of any type.
- **Parameters**:
    - _first_: First field name. Mandatory.
    - _second_: Second field name. Mandatory.
- **Error messages**:
    - If both fields do not match:
        - _{first} and {second} must match._
- **Examples**:
    ```java
    @FieldMatch(first = "password", second = "confirmPassword")
    class User {
        private String password;
        private String confirmPassword;
    }
    ```

## 🪝 Create New Constraint
For creating a new constraint you need to create both a new constraint annotation and a new validator class:

### New Constraint Annotation
Create a new annotation `YourNewConstraint` with the following template:
```java
@Documented
@Constraint(validatedBy = YourNewValidator.class)
@Target({ ElementType.FIELD })  // For filed-level constraint OR
@Target({ ElementType.TYPE })   // For class-level constraint
@Retention(RetentionPolicy.RUNTIME)
public @interface YourNewConstraint {

    String message() default "<your custom message when validation fails>.";

    // Add any other annotation methods needed by your new constraint.

}
```
- Use the `@Constraint` annotation to link `YourNewConstraint` annotation to `YourNewValidator` class.
- Define at least the `message()` method. This is the message to show when validation fails. Here you can use optionally:
  - Curly brackets to reference other annotation methods. For example: `some text with {max} value.`. The message includes the value of a sample annotation method `max()`.
  - Conditional segments based on the value of some annotation method. For example: `#if(max)some text with {max} value.#endif`. The message includes the value of an annotation method `max()` and it will be shown only if the `max()` is not empty. In this context, "empty" depends on the the annotation method type:
    - If boolean, empty means the value is false.
    - If String, empty means the text is empty.
    - If double, empty means the number is Double.MIN_VALUE or Double.MAX_VALUE.
    - If int, empty means the number is zero or Integer.MAX_VALUE.
    - If Class, empty means the class is equals to javax.lang.model.type.NullType.
    - If array, empty means the array has no elements.
  - Loop segments for constraint annotations defined as arrays. For example: `type must be#for(value) or {message}#endfor.`. That message will concatenate the `message()` of each constraint in the constraint array. The argument `value` is not meaningful.
- Add any other annotation methods needed by your new constraint.

### New Validator Class
Create a new class `YourNewValidator` with the following template:
```java
public class YourNewValidator implements ConstraintValidator<YourNewConstraint, ClassOfObjectsToValidate> {

    private Type1 annotMethod1;
    private Type2 annotMethod2;
    ...

    @Override
    public void initialize(YourNewConstraint annotation) {
        annotMethod1 = annotation.annotMethod1();
        annotMethod2 = annotation.annotMethod2();
        ...
    }

    @Override
    public boolean isValid(Object value) {
        // This condition applies for field-level constraints only
        if (value == null) {
            return true;
        }

        // Add your validation logic here
        
        return validationResult;
    }

}
```
- Implement the `ConstraintValidator<A, T>` interface, where A represents YourNewConstraint and T represents the class of the objects to validate, in this case, you can use `Object` if your validations applies to more than one class.
- Create as field members as annotation methods you have in YourNewConstraint, excluding message().
- Overrides the `initialize()` method to capture the annotation method values in your field members.
- Overrides the `isValid()` method to do the validation logic. For field-level constraints only: your first validation step must return true if the object to validate is null, because we have the annotation `@Required` to validate that condition, we don't want to evaluate that nullity here.

## 💼 Contributing
Please read our [Contributing](CONTRIBUTING.md) guide to learn and understand how to contribute to this project.


## 📄 License

This library is licensed under the MIT License. See the [LICENSE](https://github.com/sashirestela/slimvalidator/blob/main/LICENSE) file for more information.
