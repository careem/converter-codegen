package com.careem.annotation.processing.converter.generator.mapper.custom;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.mapping.Mapping;

import static com.careem.annotation.processing.converter.util.BasicExpressionUtil.methodCall;
import static java.util.Arrays.asList;

class UserDefinedConverterMapping implements Mapping {
    private final String converterClassName;
    private final ConversionMappingResult.Var var;

    UserDefinedConverterMapping(String converterClassName, ConversionMappingResult.Var var) {
        this.converterClassName = converterClassName;
        this.var = var;
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String value) {
        return new ConversionMappingResult(asList(
            new ConversionMappingResult.Import(converterClassName),
            new ConversionMappingResult.Import("org.springframework.context.annotation.Lazy"),
            new ConversionMappingResult.Import(toVar.getTypeName())),
            asList(var),
            methodCall(var.toThisRef() + ".convert", value));
    }
}