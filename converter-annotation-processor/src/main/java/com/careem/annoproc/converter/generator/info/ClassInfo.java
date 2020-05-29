package com.careem.annoproc.converter.generator.info;

import com.careem.annoproc.converter.annotation.Converter;
import lombok.Getter;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Data class for keeping class information
 */
@Getter
public class ClassInfo extends BasicClassInfo {
    private final TypeElement element;
    private final Map<String, VariableElement> fields;
    private final Map<String, ExecutableElement> methods;
    private final Set<Converter.Import> imports;

    public ClassInfo(TypeElement typeElement, Converter converter) {
        super(typeElement);
        this.element = typeElement;
        this.fields = getVariableElements(typeElement, converter.includeSuper());
        this.methods = getExecutableElements(typeElement, converter.includeSuper());
        this.imports = getImports(typeElement, converter);
    }

    public ClassInfo(TypeElement typeElement, boolean includeSuper) {
        super(typeElement);
        this.element = typeElement;
        this.fields = getVariableElements(typeElement, includeSuper);
        this.methods = getExecutableElements(typeElement, includeSuper);
        this.imports = Collections.emptySet();
    }

    public ClassInfo(ExecutableElement executableElement, Converter converter) {
        super((TypeElement)executableElement.getEnclosingElement());
        this.element = (TypeElement)executableElement.getEnclosingElement();
        this.fields = getVariableElements(executableElement);
        this.methods = getExecutableElements((TypeElement)executableElement.getEnclosingElement(), converter.includeSuper());
        this.imports = getImports((TypeElement)executableElement.getEnclosingElement(), converter);
    }

    /**
     * Gets and filters enclosed variable elements
     * (includes super class variable elements if includeSuper is true)
     * @param typeElement
     * @param includeSuper
     * @return
     */
    private static Map<String, VariableElement> getVariableElements(TypeElement typeElement, boolean includeSuper) {
        final Map<String, VariableElement> variableMap
            = toMap(ElementFilter.fieldsIn(typeElement.getEnclosedElements()), ClassInfo::filterForVariableElement);
        if (includeSuper && (typeElement.getSuperclass() instanceof DeclaredType)) {
            variableMap.putAll(getVariableElements((TypeElement)((DeclaredType) typeElement.getSuperclass()).asElement(), true));
        }
        return variableMap;
    }

    private static Map<String, VariableElement> getVariableElements(ExecutableElement executableElement) {
        return toMap((List<VariableElement>)executableElement.getParameters(), ClassInfo::filterForVariableElement);
    }

    /**
     * Gets and filters enclosed executable elements
     * (includes super class executable elements if includeSuper is true)
     * @param typeElement
     * @param includeSuper
     * @return
     */
    private static Map<String, ExecutableElement> getExecutableElements(TypeElement typeElement, boolean includeSuper) {
        final Map<String, ExecutableElement> methodMap
            = toMap(ElementFilter.methodsIn(typeElement.getEnclosedElements()), ClassInfo::filterForExecutableElement);
        if (includeSuper && (typeElement.getSuperclass() instanceof DeclaredType)) {
            methodMap.putAll(getExecutableElements((TypeElement)((DeclaredType) typeElement.getSuperclass()).asElement(), true));
        }
        return methodMap;
    }

    private static Set<Converter.Import> getImports(TypeElement typeElement, Converter converter) {
        final Set<Converter.Import> imports = new HashSet<>();
        imports.addAll(Arrays.asList(converter.imports()));
        if (converter.includeSuper() && (typeElement.getSuperclass() instanceof DeclaredType)) {
            // TODO: for now only support first converter annotation
            final TypeElement superTypeElement = (TypeElement)((DeclaredType) typeElement.getSuperclass()).asElement();
            final Converter.Import[] superImports = findFirstConverterAnnotation(superTypeElement).imports();
            imports.addAll(Arrays.asList(superImports));
        }

        return imports;
    }

    private static Converter findFirstConverterAnnotation(TypeElement typeElement) {
        return typeElement.getAnnotationsByType(Converter.class)[0];
    }

    /**
     * Filters list of elements and converts them to map
     * with key as element's simple name, and value as element itself
     * @param enclosedElements
     * @param filterPredicate
     * @param <T>
     * @return
     */
    private static <T extends Element> Map<String, T> toMap(List<T> enclosedElements, Predicate<T> filterPredicate) {
        return enclosedElements
            .stream()
            .filter(filterPredicate)
            .collect(Collectors.toMap(o -> o.getSimpleName().toString(), Function.identity(), (o, o2) -> o));
    }

    /**
     * Predicate for filtering variable elements
     * @param variableElement
     * @return
     */
    private static boolean filterForVariableElement(VariableElement variableElement) {
        return !variableElement.getModifiers().contains(Modifier.STATIC);
    }

    /**
     * Predicate for filtering executable elements
     * @param executableElement
     * @return
     */
    private static boolean filterForExecutableElement(ExecutableElement executableElement) {
        Set<Modifier> modifiers = executableElement.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) && !modifiers.contains(Modifier.STATIC);
    }
}