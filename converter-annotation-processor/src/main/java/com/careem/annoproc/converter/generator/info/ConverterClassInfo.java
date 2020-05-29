package com.careem.annoproc.converter.generator.info;

import lombok.Getter;

/**
 * Data class for keeping converter class information
 */
@Getter
public class ConverterClassInfo extends BasicClassInfo {
    private BasicClassInfo fromClass;
    private BasicClassInfo toClass;
    private BasicClassInfo interfaceToClass;
    private boolean nonNull;

    public ConverterClassInfo(BasicClassInfo fromClass, BasicClassInfo toClass, boolean nonNull) {
        this(fromClass, toClass, toClass, nonNull);
    }

    public ConverterClassInfo(BasicClassInfo fromClass, BasicClassInfo toClass, BasicClassInfo interfaceToClass, boolean nonNull) {
        super(toClass.getPackageName(), fromClass.getSimpleName() + "To" + toClass.getSimpleName() + "Converter");
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.interfaceToClass = interfaceToClass;
        this.nonNull = nonNull;
    }
}
