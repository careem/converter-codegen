package com.careem.annotation.processing.converter.generator.state;

import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.state.visitor.ClassStateVisitor;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Manages state (common information) of to-be-generated class
 */
public class ClassState {
    protected final Set<ConversionMappingResult.Import> imports;
    protected final Set<ConversionMappingResult.Var> classVars;

    public ClassState() {
        imports = new LinkedHashSet<>();
        classVars = new LinkedHashSet<>();
    }

    public Set<ConversionMappingResult.Import> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    public Set<ConversionMappingResult.Var> getClassVars() {
        return Collections.unmodifiableSet(classVars);
    }

    public void updateResult(ConversionMappingResult result) {
        imports.addAll(result.getImports());
        classVars.addAll(result.getVars());
    }

    public void accept(ClassStateVisitor visitor) {
        visitor.visit(this);
    }
}
