package com.careem.annotation.processing.converter.generator.state;

import com.careem.annotation.processing.converter.annotation.Converter;
import com.careem.annotation.processing.converter.annotation.ConverterMapping;
import com.careem.annotation.processing.converter.generator.info.ClassInfo;
import com.careem.annotation.processing.converter.generator.info.VariableInfo;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.helper.generator.Constants;
import com.careem.annotation.processing.converter.util.BasicExpressionUtil;

/**
 * Manages state of converter class to be generated in builder format
 */
public class BuilderConverterClassState extends ConverterClassState {
    private static final String INDENTS = Constants.INDENTS_MAP.get(3);
    private final StringBuilder builder;

    public BuilderConverterClassState(ClassInfo targetClass, ClassInfo sourceClass, Converter converter) {
        super(targetClass, sourceClass, converter);
        builder = new StringBuilder();
    }

    public void appendCode(VariableInfo toVar, String expression) {
        builder.append(Constants.LINE_SEPARATOR)
            .append(INDENTS)
            .append('.')
            .append(toVar.getSimpleName())
            .append('(')
            .append(expression)
            .append(')');
    }

    public void appendCode(VariableInfo toVar, String srcElementName, ConversionMappingResult result, ConverterMapping converterMapping) {
        if (converterMapping != null && converterMapping.nullCheck()) {
            appendCode(toVar, BasicExpressionUtil.nullCheck(srcElementName, result.getExpression(), null));
        } else {
            appendCode(toVar, result.getExpression());
        }
    }

    public String buildPropsCode() {
        return builder.toString();
    }
}
