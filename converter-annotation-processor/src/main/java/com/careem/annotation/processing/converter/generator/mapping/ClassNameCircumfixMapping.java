package com.careem.annotation.processing.converter.generator.mapping;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * For building simple expressions {@link ConversionMappingResult}
 * in {className}+{prefix}+{value}+{suffix} format
 */
public class ClassNameCircumfixMapping extends CircumfixMapping {
    public ClassNameCircumfixMapping(String prefix, String suffix, List<ConversionMappingResult.Import> imports, List<ConversionMappingResult.Var> vars) {
        super(prefix, suffix, imports, vars);
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String value) {
        final StringBuilder builder = new StringBuilder();
        builder.append(toVar.getTypeElementSimpleName());
        if (prefix != null) {
            builder.append(prefix);
        }
        builder.append(value);
        if (suffix != null) {
            builder.append(suffix);
        }

        final List<ConversionMappingResult.Import> newImports = new ArrayList<>();
        newImports.addAll(imports);
        newImports.add(new ConversionMappingResult.Import(toVar.getTypeElementName()));
        return new ConversionMappingResult(newImports, vars, builder.toString());
    }
}
