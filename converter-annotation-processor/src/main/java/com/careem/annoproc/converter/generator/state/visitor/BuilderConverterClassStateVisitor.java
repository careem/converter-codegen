package com.careem.annoproc.converter.generator.state.visitor;

import com.careem.annoproc.converter.annotation.ConverterMapping;
import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.info.ClassInfo;
import com.careem.annoproc.converter.generator.info.MethodInfo;
import com.careem.annoproc.converter.generator.info.VariableInfo;
import com.careem.annoproc.converter.generator.mapper.Mapper;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.generator.state.BuilderConverterClassState;
import com.careem.annoproc.converter.generator.state.ConverterClassState;
import com.careem.annoproc.converter.util.ConverterAnnotationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.function.Function;

/**
 * Visits {@link BuilderConverterClassState} and
 * updates its state for generating converter class in builder format
 */
public class BuilderConverterClassStateVisitor implements ClassStateVisitor<BuilderConverterClassState> {
    private final List<Mapper> mappers;

    public BuilderConverterClassStateVisitor(List<Mapper> mappers) {
        this.mappers = mappers;
    }

    @Override
    public void visit(BuilderConverterClassState state) {
        if (!ConverterAnnotationUtil.getTypeElementForSourceClass(state.getConverter()).getQualifiedName().toString().equals(Void.class.getCanonicalName())) {
            for (VariableElement targetVariable : state.getTargetClass().getFields().values()) {
                final ConverterVarInfo converterVarInfo = getConverterVarInfoForTarget(targetVariable, state);

                if (converterVarInfo == null) {
                    continue;
                }

                process(converterVarInfo, state);
            }
        } else if (!ConverterAnnotationUtil.getTypeElementForTargetClass(state.getConverter()).getQualifiedName().toString().equals(Void.class.getCanonicalName())) {
            for (VariableElement sourceVariable : state.getSourceClass().getFields().values()) {
                final ConverterVarInfo converterVarInfo = getConverterVarInfoForSource(sourceVariable, state);

                if (converterVarInfo == null) {
                    continue;
                }

                process(converterVarInfo, state);
            }
        } else {
            throw new IllegalArgumentException("neither sourceClass nor targetClass found");
        }
    }

    private void process(ConverterVarInfo converterVarInfo, BuilderConverterClassState state) {
        final ConverterMapping converterMapping = converterVarInfo.getConverterMapping();
        final VariableInfo toVar = converterVarInfo.getTo();

        if (converterMapping != null && !"".equals(converterMapping.value())) {
            state.appendCode(toVar, converterMapping.value());
            return;
        }

        final BasicEnclosedElementInfo fromVar = converterVarInfo.getFrom();
        final String getter = "from" + (fromVar instanceof VariableSelfInfo ? "" : "." + fromVar.getGetterSimpleName());
        ConversionMappingResult result = null;
        for (Mapper mapper : mappers) {
            result = mapper.getFor(3, fromVar, toVar, getter);
            if (result != null) {
                break;
            }
        }

        if (result == null) {
            throw new IllegalStateException("Conversion: missed " +
                state.getConverterClass().getFromClass().getName() + "#" + fromVar.getGetterSimpleName() +
                " to " + state.getConverterClass().getToClass().getName() + "#" + toVar.getSimpleName() +
                " conversion -- because: " + fromVar.getTypeName() + " - " + toVar.getTypeName() +
                " -- " + (fromVar.getTypeName().equals(toVar.getTypeName())));
        }

        state.updateResult(result);
        state.appendCode(toVar, getter, result, converterMapping);
    }

    private ConverterVarInfo getConverterVarInfoForTarget(VariableElement targetVariable, ConverterClassState state) {
        final VariableInfo targetVar = new VariableInfo(targetVariable);
        ConverterMapping converterMapping
            = getConverterMappingForSourceClass(targetVar.getElement(), state.getSourceClass().getElement());

        if (converterMapping != null && converterMapping.skip()) {
            return null;
        }

        final BasicEnclosedElementInfo sourceVar
            = getEnclosedElementInfoForMappingForTarget(targetVar, state.getSourceClass(), converterMapping);

        if (sourceVar != null) {
            return new ConverterVarInfo(sourceVar, targetVar, converterMapping);
        }

        if (converterMapping == null && !state.getConverter().ignoreUnknownMapping()) {
            throw new IllegalArgumentException("No mapping found for " +
                state.getTargetClass().getName() + ":" + targetVar.getGetterSimpleName());
        }

        return new ConverterVarInfo(null, targetVar, converterMapping);
    }

    private ConverterVarInfo getConverterVarInfoForSource(VariableElement sourceVariable, ConverterClassState state) {
        final VariableInfo sourceVar = new VariableInfo(sourceVariable);
        ConverterMapping converterMapping
            = getConverterMappingForTargetClass(sourceVar.getElement(), state.getTargetClass().getElement());

        if (converterMapping != null && converterMapping.skip()) {
            return null;
        }

        final VariableInfo targetVar
            = getEnclosedElementInfoForMappingForSource(sourceVar, state.getTargetClass(), converterMapping);

        if (targetVar != null) {
            return new ConverterVarInfo(sourceVar, targetVar, converterMapping);
        }

        if (converterMapping == null && !state.getConverter().ignoreUnknownMapping()) {
            throw new IllegalArgumentException("No mapping found for " +
                state.getSourceClass().getName() + ":" + sourceVar.getGetterSimpleName());
        }

        return new ConverterVarInfo(null, sourceVar, converterMapping);
    }

    private BasicEnclosedElementInfo getEnclosedElementInfoForMappingForTarget(
        VariableInfo targetVar,
        ClassInfo classInfo,
        ConverterMapping converterMapping) {

        String fromVarSimpleName = "";
        String fromMethodName = "";

        if (converterMapping != null) {
            fromVarSimpleName = converterMapping.field();
            fromMethodName = converterMapping.method();
        }

        if ("".equals(fromVarSimpleName)) {
            fromVarSimpleName = targetVar.getSimpleName();
        }

        if (!"".equals(fromMethodName)) {
            final ExecutableElement executableElement = classInfo.getMethods().get(fromMethodName);
            if (executableElement != null) {
                return new MethodInfo(executableElement);
            }
        }

        final VariableElement variableElement = classInfo.getFields().get(fromVarSimpleName);

        if (variableElement != null) {
            return new VariableInfo(variableElement);
        }

        if (converterMapping != null && "".equals(converterMapping.value())) {
            return new VariableSelfInfo(classInfo.getElement().asType());
        }

        return null;
    }

    private VariableInfo getEnclosedElementInfoForMappingForSource(
        VariableInfo sourceVar,
        ClassInfo classInfo,
        ConverterMapping converterMapping) {

        String toVarSimpleName = "";

        if (converterMapping != null) {
            toVarSimpleName = converterMapping.field();
        }

        if ("".equals(toVarSimpleName)) {
            toVarSimpleName = sourceVar.getSimpleName();
        }

// TODO: support for constructor to be added
//        if (!"".equals(fromMethodName)) {
//            final ExecutableElement executableElement = classInfo.getMethods().get(fromMethodName);
//            if (executableElement != null) {
//                return new MethodInfo(executableElement);
//            }
//        }

        final VariableElement variableElement = classInfo.getFields().get(toVarSimpleName);

        if (variableElement != null) {
            return new VariableInfo(variableElement);
        }

// TODO: support for self to be added
//        if (converterMapping != null && "".equals(converterMapping.value())) {
//            return new VariableSelfInfo(classInfo.getElement().asType());
//        }

        return null;
    }

    private ConverterMapping getConverterMappingForSourceClass(
        VariableElement targetElement,
        TypeElement sourceClassTypeElement) {

        return getConverterMappingFor(
            targetElement,
            sourceClassTypeElement,
            ConverterAnnotationUtil::getTypeElementForSourceClass);
    }

    private ConverterMapping getConverterMappingForTargetClass(
        VariableElement sourceElement,
        TypeElement targetClassTypeElement) {

        return getConverterMappingFor(
            sourceElement,
            targetClassTypeElement,
            ConverterAnnotationUtil::getTypeElementForTargetClass);
    }

    private ConverterMapping getConverterMappingFor(
        VariableElement element,
        TypeElement classTypeElement,
        Function<ConverterMapping, TypeElement> functionToGetTypeElement) {

        ConverterMapping voidSourceConverterMapping = null;
        for (ConverterMapping annotation : element.getAnnotationsByType(ConverterMapping.class)) {
            final TypeElement mappingClassTypeElement = functionToGetTypeElement.apply(annotation);
            if (mappingClassTypeElement.equals(classTypeElement)) {
                return annotation;
            }

            if (mappingClassTypeElement.getQualifiedName().toString()
                .equals(Void.class.getCanonicalName())) {
                if (voidSourceConverterMapping != null) {
                    throw new IllegalArgumentException("Multiple ConverterMapping annotations with Void");
                }
                voidSourceConverterMapping = annotation;
            }
        }

        return voidSourceConverterMapping;
    }

    /**
     * For keeping variable info - used when referring fromClass itself instead of its member
     */
    private static class VariableSelfInfo extends BasicEnclosedElementInfo {
        public VariableSelfInfo(TypeMirror typeMirror) {
            super(typeMirror, null);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class ConverterVarInfo {
        private final BasicEnclosedElementInfo from;
        private final VariableInfo to;
        private final ConverterMapping converterMapping;
    }
}
