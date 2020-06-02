package com.careem.annotation.processing.converter.generator.mapping;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;

import java.util.Collections;
import java.util.List;

/**
 * For building simple expressions {@link ConversionMappingResult}
 * in {prefix}+{value}+{suffix} format
 */
public class CircumfixMapping implements Mapping {
    protected final String prefix;
    protected final String suffix;
    protected final List<ConversionMappingResult.Import> imports;
    protected final List<ConversionMappingResult.Var> vars;

    public CircumfixMapping(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.imports = Collections.emptyList();
        this.vars = Collections.emptyList();
    }

    public CircumfixMapping(
        String prefix,
        String suffix,
        List<ConversionMappingResult.Import> imports,
        List<ConversionMappingResult.Var> vars) {

        this.prefix = prefix;
        this.suffix = suffix;
        this.imports = Collections.unmodifiableList(imports);
        this.vars = Collections.unmodifiableList(vars);
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String value) {
        final StringBuilder builder = new StringBuilder();
        if (prefix != null) {
            builder.append(prefix);
        }
        builder.append(value);
        if (suffix != null) {
            builder.append(suffix);
        }
        return new ConversionMappingResult(imports, vars, builder.toString());
    }
}
