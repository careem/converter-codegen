package com.careem.annoproc.converter.annotation;

import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If default mapping is not going to work for a field,
 * then we can add @ConverterMapping annotation on that field
 * and configure custom mapping as needed.
 * For example, we have a class Author with field authorName,
 * and a class Person with field name.
 * So, for converting Author to Person,
 * we need to map authorName of Author to name field of Person.
 * This can be done by adding the annotation on the field as follows
 *     \@ConverterMapping(sourceClass = Author.class, field = "authorName")
 *     private String name;
 */
@Retention(RetentionPolicy.SOURCE)
@Target({
    ElementType.FIELD,
    ElementType.PARAMETER
})
@Repeatable(ConverterMappings.class)
public @interface ConverterMapping {
    Class sourceClass() default Void.class;
    Class targetClass() default Void.class;

    /**
     * If value is not empty then use the value as-it-is as expression
     * @return String
     */
    String value() default "";

    /**
     * If value is empty, but field is not empty then use field name
     * for generating {@link ConversionMappingResult} expression
     * @return String
     */
    String field() default "";

    /**
     * If value and field are empty, but method is not empty then use method
     * for generating {@link ConversionMappingResult} expression
     * @return String
     */
    String method() default "";

    /**
     * Use check for null before converting and/or setting value of the target field
     * @return boolean
     */
    boolean nullCheck() default false;

    /**
     * If skip is true, then field is mapping is not added in the converter
     * @return boolean
     */
    boolean skip() default false;
}
