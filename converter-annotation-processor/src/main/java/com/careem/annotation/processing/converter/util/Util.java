package com.careem.annotation.processing.converter.util;

import com.careem.annotation.processing.converter.annotation.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {
    public static TypeElement asTypeElement(TypeMirror typeMirror) {
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }

    public static Converter[] getConverters(TypeElement targetClassElement) {
        return targetClassElement.getAnnotationsByType(Converter.class);
    }

    public static Converter[] getConverters(ExecutableElement targetConstructorElement) {
        return targetConstructorElement.getAnnotationsByType(Converter.class);
    }
}
