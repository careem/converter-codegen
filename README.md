![Java CD with Maven](https://github.com/careem/converter-codegen/workflows/Java%20CD%20with%20Maven/badge.svg)

# converter-codegen
Generates converters through annotation processing

---

### How to use
#### Converter code generation:
Maven dependency:
```xml
<dependency>
  <groupId>io.github.careem</groupId>
  <artifactId>converter-annotation-processor</artifactId>
  <version>${converter-codegen-version}</version>
  <scope>compile</scope>
</dependency>
```
##### Example 1
Consider the following classes:
```java
package com.careem.dtos;

public class Dto1 {
  Integer value;
}
```
```java
package com.careem.a;

import com.careem.dtos.Dto1;
import lombok.Getter;

@Getter
public class ClassA {
  Integer field1;
  String field2;
  Integer field3;
  Dto1 field4;
}
```
```java
package com.careem.b;

import lombok.Builder;

@Builder
public class ClassB {
  Integer field1;
  Integer field2;
  String field3;
  String field4;
}
```
To generate a `ClassA` to `ClassB` converter, add `@Converter` annotation on `ClassB` as follows:

```java
@Converter(sourceClass = ClassA.class)
@Builder
public class ClassB {
  //...
}
```
Compiling the code will generate
```java
package com.careem.a;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.careem.b.ClassB;
import com.careem.dtos.Dto1;
import com.google.gson.Gson;

@Component
public class ClassAToClassBConverter implements Converter<ClassA, ClassB> {
    private final Gson gson;

    public ClassAToClassBConverter(Gson gson) {
        this.gson = gson;
    }

    @NonNull
    @Override
    public ClassB convert( ClassA from) {
        return ClassB.builder()
            .field1(from.getField1())
            .field3(String.valueOf(from.getField3()))
            .field2(Integer.valueOf(from.getField2()))
            .field4(this.gson.toJson(from.getField4(), Dto1.class))
            .build();
    }
}

```

##### Example 2 (using hard coded value, and imports)
Consider the following classes:
```java
package com.careem.a;

import com.careem.dtos.Dto1;
import lombok.Getter;

@Getter
public class ClassA {
  Integer field1;
  String field2;
  Integer field3;
}
```
```java
package com.careem.b;

import lombok.Builder;

@Builder
public class ClassB {
  Integer field1;
  Integer field2;
  String field3;
  String field4;
}
```
To generate a `ClassA` to `ClassB` converter, and set field4 value from some other source (e.g. com.careem.Constants.FIELD4), add `@Converter` annotation on `ClassB`, and `@ConveterMapping` on `field4` as follows:

```java
@Converter(sourceClass = ClassA.class, imports = @Converter.Import(value = "com.careem.Constants"))
@Builder
public class ClassB {
  //...
  @ConverterMapping(sourceClass = ClassA.class, value = "Constants.FIELD4")
  String field4
}
```
Compiling the code will generate
```java
package com.careem.a;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.careem.b.ClassB;
import com.careem.dtos.Dto1;
import com.google.gson.Gson;

@Component
public class ClassAToClassBConverter implements Converter<ClassA, ClassB> {
    private final Gson gson;

    public ClassAToClassBConverter(Gson gson) {
        this.gson = gson;
    }

    @NonNull
    @Override
    public ClassB convert( ClassA from) {
        return ClassB.builder()
            .field1(from.getField1())
            .field3(String.valueOf(from.getField3()))
            .field2(Integer.valueOf(from.getField2()))
            .field4(Constants.FIELD4)
            .build();
    }
}

```
##### Example 3 (creating multiple converters)
Consider the following
```java
class ClassA {
    String fieldA;
}
```
```java
class ClassB {
    String fieldB;
}
```
```java
class ClassC {
    String fieldC;
}
```
To generate `ClassB` to `ClassA` converter as well as `ClassC` to `ClassA` converter, we annotate `ClassA` as follows
```java
@Converter(sourceClass = ClassB.class)
@Converter(sourceClass = ClassC.class)
class ClassA{
    @ConverterMapping(sourceClass = ClassB.class, field = "fieldB")
    @ConverterMapping(sourceClass = ClassC.class, field = "fieldC")
    String fieldA;
}
```

##### Example 4 (using interfaceTargetClass)
Consider the following
```java
@Getter
class ClassA {
}
```
```java
abstract class ClassB {
}
```
```java
@Builder
@Converter(sourceClass = ClassA.class)
class ClassC extends ClassB {
}
```
then compiling will generate following converter
```java
class ClassAToClassCConverter implements Converter<ClassA, ClassC> {
    @NonNull ClassC convert(ClassA from) {
        return ClassC.builder()
            .build();
    }
}
```
but, if we instead annotate ClassC as follows
```java
@Builder
@Converter(sourceClass = ClassA.class, interfaceTargetClass = ClassB.class)
class ClassC extends ClassB {
}
```
then compiling will generate following conveter
```java
class ClassAToClassCConverter implements Converter<ClassA, ClassB> {
    @NonNull ClassB convert(ClassA from) {
        return ClassC.builder()
            .build();
    }
}
```

##### Example 5 (when `@Builder` is added on constructor)
Consider the following
```java
@AllArgsConstructor
class ClassX {
    String field1;
    String field2;
}
```
```java
class ClassA {
    String field1;
}
```
```java
class ClassB extends ClassA {
    String field2;
    
    @Builder
    public ClassB(String field1, String field2) {
        super(field1);
        this.field2 = field2;
    } 
}
```
then to generate `ClassX` to `ClassB` converter that considers all fields in the builder of `ClassB` (here `field1` and `field2`), we need to add the `@Converter` annotation on the constructor like
```java
class ClassB extends ClassA {
    String field2;
    
    @Converter(sourceClass = ClassX.class)
    @Builder
    public ClassB(String field1, String field2) {
        super(field1);
        this.field2 = field2;
    } 
}
```

##### Example 6 (phantom converter)
Consider the following:
```java
abstract class ClassA {
}
```
```java
abstract class ClassB {
}
```
```java
class ClassAX extends ClassA {
}
```
```java
class ClassBX extends ClassB {
}
```
assuming there exists (or is auto-generated) `ClassAX` to `ClassBX` converter (with `ClassB` as second type argument in `Converter`) as follows:
```java
class ClassAXToClassBXConverter implements Converter<ClassAX, ClassB> {
    @Override
    public ClassB convert(ClassAX from) {
        return ClassBX.builder()
        // ... set properties
            .build();
    }
}
```
then consider
```java
class ClassC {
    ClassA classA; // this will have only derived class objects
}
```
```java
@Converter(sourceClass = ClassC.class)
class ClassD {
    ClassB classB; // this will have only derived class objects
}
```
the above will fail the compilation because converter annotation processor won't be able to find `ClassA` to `ClassB` converter, but we already have a converter to convert from `ClassAX` to `ClassBX`. To generate `ClassC` to `ClassD` converter, we will need to make the converter annotation processor assume that there already exists a `ClassA` to `ClassB` converter. Which can be achieved by `phantom` field as follows:
```java
@Converter(sourceClass = ClassA.class, phantom = true)
class ClassB {
    //...
}
```
the above will not generate a converter but would allow continuing the annotation processing.
