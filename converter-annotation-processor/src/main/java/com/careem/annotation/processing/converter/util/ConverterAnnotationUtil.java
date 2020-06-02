package com.careem.annotation.processing.converter.util;

import com.careem.annotation.processing.converter.annotation.Converter;
import com.careem.annotation.processing.converter.annotation.ConverterConfiguration;
import com.careem.annotation.processing.converter.annotation.ConverterMapping;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Utility class for getting information from converter annotations
 */
public class ConverterAnnotationUtil {
    private ConverterAnnotationUtil() {
    }

    public static TypeElement getTypeElementForSourceClass(Converter converterAnnotation) {
        try {
            converterAnnotation.sourceClass();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get sourceClass");
    }

    public static TypeElement getTypeElementForTargetClass(Converter converterAnnotation) {
        try {
            converterAnnotation.targetClass();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get targetClass");
    }

    public static TypeElement getTypeElementForInterfaceTargetClass(Converter converterAnnotation) {
        try {
            converterAnnotation.interfaceTargetClass();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        return null;
    }

    public static TypeElement getTypeElementForSourceClass(ConverterMapping converterAnnotation) {
        try {
            converterAnnotation.sourceClass();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get sourceClass");
    }

    public static TypeElement getTypeElementForTargetClass(ConverterMapping converterAnnotation) {
        try {
            converterAnnotation.targetClass();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get targetClass");
    }

    public static TypeElement getFromTypeElement(ConverterConfiguration.Mapping mapping) {
        try {
            mapping.from();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get from class");
    }

    public static TypeElement getToTypeElement(ConverterConfiguration.Mapping mapping) {
        try {
            mapping.to();
        } catch (MirroredTypeException e) {
            return handle(e);
        }
        throw new IllegalArgumentException("failed to get to class");
    }

    private static TypeElement handle(MirroredTypeException e) {
        return (TypeElement) ((DeclaredType) e.getTypeMirror()).asElement();
    }
}
