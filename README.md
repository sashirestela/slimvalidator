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

  @ObjectType(baseClass = {String.class})
  @ObjectType(schema = Schema.COLL, baseClass = {String.class}, maxSize = 3)
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
reference type must be one of String or Collection<String> and size at most 3.
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
    - _min_: The lowest value of the range. By default is Double.MAX_VALUE.
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
- **Description**: Checks that the type of an object matches a specific schema pattern. Supports direct objects, collections, nested collections, maps, and maps with collection values.
- **Applies to**: Fields of the Object type, including various collection and map structures.
- **Parameters**:
    - _schema_: The schema pattern to validate against. Options: DIRECT, COLL, COLL_COLL, MAP, MAP_COLL. By default is DIRECT.
    - _baseClass_: Array of candidate base classes for the field. Mandatory.
    - _keyClass_: The class type for map keys when using MAP or MAP_COLL schema. By default is void.class.
    - _maxSize_: The maximum size of the outer collection or map. By default is Integer.MAX_VALUE.
    - _maxInnerSize_: The maximum size of inner collections. By default is Integer.MAX_VALUE.
    - _maxChecks_: The maximum number of items to check for performance optimization. By default is 20.
    - _allowNull_: Whether to allow null values in collections or maps. By default is true.
    - _allowInnerNull_: Whether to allow null values in inner collections. By default is true.
- **Schema Types**:
    - _DIRECT_: Validates direct object types (baseClass)
    - _COLL_: Validates Collection\<baseClass>
    - _COLL_COLL_: Validates Collection<Collection\<baseClass>>
    - _MAP_: Validates Map<keyClass, baseClass>
    - _MAP_COLL_: Validates Map<keyClass, Collection\<baseClass>>
- **Error messages**:
    - Dynamically generated based on schema type and configuration parameters
- **Example**:
    ```java
    // directReference could be: String or Integer
    @ObjectType(baseClass = {String.class, Integer.class})
    private Object directReference;

    // mapReference could be: Map<String, Double> or Map<String, Integer>
    @ObjectType(schema = Schema.MAP, keyClass = String.class, baseClass = {Double.class, Integer.class})
    private Object mapReference;

    // multiSchemaReference could be: String or List<String> or List<List<String>>
    @ObjectType(baseClass = {String.class})
    @ObjectType(schema = Schema.COLL, baseClass = {String.class}, maxSize = 2)
    @ObjectType(schema = Schema.COLL_COLL, baseClass = {String.class}, maxSize = 2)
    private Object multiSchemaReference;
    ```

### @Valid
- **Description**: Flag to do nested validation for fields without any constraint.
- **Applies to**: Fields of custom classes that do not have any constraint but it requires to validate their nested fields. Any field-level constraint enable nested validation automatically.
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

    // Add any annotation methods needed by your new constraint.

}
```
- Use the `@Constraint` annotation to link `YourNewConstraint` annotation to `YourNewValidator` class.
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
    public String getMessage() {
        // This the message to output when object violates the constraint.
        // Prepare it using the annotation methods values gathered in initialize().
        // Example:
        // return "Custom message with " + annotMethod1 + " and " + annotMethod2;
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
- Create as field members as annotation methods you have in YourNewConstraint.
- Override the `initialize()` method to capture the annotation method values in your field members.
- Override the `getMessage()` method to build the error message using the annotation method values.
- Override the `isValid()` method to do the validation logic. For field-level constraints only: your first validation step must return true if the object to validate is null, because we have the annotation `@Required` to validate that condition, we don't want to evaluate that nullity here.

## 💼 Contributing
Please read our [Contributing](CONTRIBUTING.md) guide to learn and understand how to contribute to this project.


## 📄 License

This library is licensed under the MIT License. See the [LICENSE](https://github.com/sashirestela/slimvalidator/blob/main/LICENSE) file for more information.
