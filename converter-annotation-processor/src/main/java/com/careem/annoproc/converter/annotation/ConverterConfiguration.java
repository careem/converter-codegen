package com.careem.annoproc.converter.annotation;

import com.careem.annoproc.converter.generator.mapping.CircumfixMapping;
import com.careem.annoproc.converter.generator.mapping.ClassNameCircumfixMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation is for setting up user defined configurations for converters
 * to be generated. The annotation processor finds all the class elements
 * annotated with this annotation, and processes them.
 *
 * For example, we have an interface IntegerEnumType with getCode() method returning an Integer,
 * a ClassA which has field jobType of type JobType (which implements some interface IntegerEnumType)
 * and a ClassB which has field jobType of type Integer.
 * For mapping ClassA::jobType to ClassB::jobType, we want to preconfigure the mapping,
 * so we can directly configure mapping on interface level as follows:
 * @ConverterConfiguration(
 *     mappings = {
 *         @ConverterConfiguration.Mapping(
 *             from = IntegerEnumType.class,
 *             to = Integer.class,
 *             suffix = ".getCode()"
 *         )
 *     }
 * )
 * Now, it will work for all classes implementing IntegerEnumType
 */
@Target(ElementType.TYPE)
public @interface ConverterConfiguration {

    /**
     * packages to be scanned to find existing converters
     * @return
     */
    String[] packages() default {};

    Mapping[] mappings() default {};

    @interface Mapping {
        /**
         * Class being converted from
         */
        Class from();

        /**
         * Class being converted to
         */
        Class to();

        /**
         * prefix field use in {@link CircumfixMapping}
         */
        String prefix() default "";

        /**
         * suffix field used in {@link CircumfixMapping}
         */
        String suffix() default "";

        /**
         * Decides which mapping class to use
         */
        Strategy strategy() default Strategy.CIRCUMFIX;

        enum Strategy {
            /**
             * Maps to {@link CircumfixMapping}
             */
            CIRCUMFIX,

            /**
             * Maps to {@link ClassNameCircumfixMapping}
             */
            CLASS_NAME_CIRCUMFIX
        }
    }
}
