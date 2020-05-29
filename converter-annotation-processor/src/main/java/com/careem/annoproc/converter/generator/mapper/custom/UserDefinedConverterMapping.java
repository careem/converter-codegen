package com.careem.annoproc.converter.generator.mapper.custom;

import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.generator.mapping.Mapping;

import static com.careem.annoproc.converter.util.BasicExpressionUtil.methodCall;
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