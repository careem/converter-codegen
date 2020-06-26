# converter-annotation-processor

![ci](https://github.com/careem/converter-codegen/workflows/ci/badge.svg?branch=0.6.x)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.careem/converter-annotation-processor.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.careem/converter-annotation-processor)

> A Java library that auto-generates converters.

![screenshots](https://raw.githubusercontent.com/careem/converter-codegen/gh-pages/img/screenshots.gif)

## Setup

Add the following dependency in your `pom.xml` file:
```xml
<dependency>
  <groupId>io.github.careem</groupId>
  <artifactId>converter-annotation-processor</artifactId>
  <version>0.7.0</version>
</dependency>
```
That's it, you are ready to generate converters!

## Usage

To generate a `ClassA` object to `ClassB` object converter:
- add `@Converter(sourceClass = ClassA.class)` annotation on `ClassB` 
- `mvn clean install ...`
- `ClassAToClassBConverter` gets auto-generated!

Please visit [usage](https://careem.github.io/converter-codegen/usage) for more.

## Contributing

Please visit [contributing](https://careem.github.io/converter-codegen/contributing).
