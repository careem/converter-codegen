package com.careem.annotation.processing.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for generating converter class.
 * For example to generate a converter class to convert
 * ClassA to ClassB then we annotate ClassB with
 * this annotation as follows
 * \@{Converter(sourceClass = ClassA.class)
 * public class ClassB {
 *     //...
 * }
 * Rest of the @Converter properties are optional.
 */
@Target(value = {
    ElementType.TYPE,
    ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.CLASS)
@Repeatable(Converters.class)
public @interface Converter {
    /**
     * This represents the class to convert from (to the class which is annotated)
     *
     * @return {@link Class}
     */
    Class sourceClass() default Void.class;

    /**
     * This represents the class to convert to (from the class which is annotated)
     * Note: preference given to sourceClass when both sourceClass and targetClass are present
     *
     * @return {@link Class}
     */
    Class targetClass() default Void.class;

    /**
     * If present then Converter interface implementation
     * will have that class as second type argument, otherwise it will
     * use the target class (the class which is being annotated) as the
     * second type argument.
     *
     * @return {@link Class}
     */
    Class interfaceTargetClass() default Void.class;

    /**
     * If true then compilation will assume there would be an
     * associated converter generated, but won't actually generate it
     *
     * @return {@link boolean}
     */
    boolean assumeExists() default false;

    /**
     * If true then unknown mappings are ignored
     * else exception is thrown for unknown mappings
     *
     * @return {@link boolean}
     */
    boolean ignoreUnknownMapping() default false;

    /**
     * If true then super class members are included for mapping
     *
     * @return {@link boolean}
     */
    boolean includeSuper() default false;

    /**
     * If true then the convert argument and return type will be annotated with @NonNull
     * else if check will be added to return null
     *
     * @return {@link boolean}
     */
    boolean nonNull() default false;

    /**
     * Additional imports for generated converter
     *
     * @return {@link Import}[]
     */
    Import[] imports() default {};

    @interface Import {
        String value();
        boolean isStatic() default false;
    }
}
