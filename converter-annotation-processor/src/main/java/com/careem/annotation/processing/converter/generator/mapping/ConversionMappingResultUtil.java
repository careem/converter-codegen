package com.careem.annotation.processing.converter.generator.mapping;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.helper.generator.util.NamingUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.careem.annotation.processing.converter.util.BasicExpressionUtil.methodCall;
import static java.util.Arrays.asList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConversionMappingResultUtil {
    private static final String CONVERSION_SERVICE_NAME = "org.springframework.core.convert.ConversionService";
    private static final String CONVERSION_SERVICE_SIMPLE_NAME = "ConversionService";
    private static final String LAZY_NAME = "org.springframework.context.annotation.Lazy";

    public static ConversionMappingResult createGenericConversionMappingResult(
        BasicEnclosedElementInfo toVar,
        String expr) {

        final ConversionMappingResult.Var conversionVar
            = new ConversionMappingResult.Var(
            NamingUtil.camelCase(CONVERSION_SERVICE_SIMPLE_NAME),
            CONVERSION_SERVICE_SIMPLE_NAME,
            true);

        return new ConversionMappingResult(asList(
            new ConversionMappingResult.Import(CONVERSION_SERVICE_NAME),
            new ConversionMappingResult.Import(LAZY_NAME),
            new ConversionMappingResult.Import(toVar.getTypeElementName())),
            asList(conversionVar),
            methodCall(
                conversionVar.toThisRef() + ".convert",
                expr,
                toVar.getTypeElementSimpleName() + ".class"));
    }
}
