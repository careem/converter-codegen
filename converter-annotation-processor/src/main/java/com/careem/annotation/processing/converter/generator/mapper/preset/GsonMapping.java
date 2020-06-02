package com.careem.annotation.processing.converter.generator.mapper.preset;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.mapping.Mapping;

import static com.careem.annotation.processing.converter.util.BasicExpressionUtil.methodCall;
import static com.careem.annotation.processing.converter.util.Types.GSON;
import static com.careem.annotation.processing.converter.util.Types.STRING;
import static java.util.Arrays.asList;

class GsonMapping implements Mapping {
    private static final String DOT_CLASS = ".class";
    private static final ConversionMappingResult.Var GSON_VAR = new ConversionMappingResult.Var("gson", "Gson");

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String expr) {
        if (fromVar.getTypeName().equals(STRING)) {
            return new ConversionMappingResult(
                asList(new ConversionMappingResult.Import(GSON), new ConversionMappingResult.Import(toVar.getTypeName())),
                asList(GSON_VAR),
                methodCall(GSON_VAR.toThisRef() + ".fromJson", expr, toVar.getTypeElementSimpleName() + DOT_CLASS));
        }

        if (toVar.getTypeName().equals(STRING)) {
            return new ConversionMappingResult(
                asList(new ConversionMappingResult.Import(GSON), new ConversionMappingResult.Import(fromVar.getTypeName())),
                asList(GSON_VAR),
                methodCall(GSON_VAR.toThisRef() + ".toJson", expr, fromVar.getTypeElementSimpleName() + DOT_CLASS));
        }

        return null;
    }
}
