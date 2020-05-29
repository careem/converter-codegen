package com.careem.annoproc.converter.generator.state;

import com.careem.annoproc.converter.annotation.Converter;
import com.careem.annoproc.converter.annotation.ConverterMapping;
import com.careem.annoproc.converter.generator.info.ClassInfo;
import com.careem.annoproc.converter.generator.info.VariableInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.helper.generator.Constants;

import static com.careem.annoproc.converter.util.BasicExpressionUtil.nullCheck;
import static com.careem.annoproc.helper.generator.Constants.INDENTS_MAP;

/**
 * Manages state of converter class to be generated in builder format
 */
public class BuilderConverterClassState extends ConverterClassState {
    private static final String INDENTS = INDENTS_MAP.get(3);
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
            appendCode(toVar, nullCheck(srcElementName, result.getExpression(), null));
        } else {
            appendCode(toVar, result.getExpression());
        }
    }

    public String buildPropsCode() {
        return builder.toString();
    }
}
