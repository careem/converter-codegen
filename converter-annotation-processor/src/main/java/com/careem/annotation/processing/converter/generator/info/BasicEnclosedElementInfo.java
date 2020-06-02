package com.careem.annotation.processing.converter.generator.info;

import lombok.Getter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Data class for keeping mapping variable information
 */
@Getter
public class BasicEnclosedElementInfo {
    private final DeclaredType type;
    private final TypeElement typeElement;
    private final String typeName;
    private final String typeElementName;
    private final String typeElementSimpleName;
    private final String getterSimpleName; // TODO: move outside base class

    @SuppressWarnings("fb-contrib:STT_TOSTRING_STORED_IN_FIELD")
    public BasicEnclosedElementInfo(TypeMirror typeMirror, String getterSimpleName) {
        if (typeMirror instanceof DeclaredType) {
            this.type = (DeclaredType) typeMirror;
            this.typeElement = (TypeElement) type.asElement();
            this.typeName = sanitizeTypeName(type.toString());

            this.typeElementName = this.typeElement.getQualifiedName().toString();
            this.typeElementSimpleName = this.typeElement.getSimpleName().toString();
        } else {
            this.type = null;
            this.typeElement = null;
            this.typeName = sanitizeTypeName(typeMirror.toString());
            this.typeElementName = null;
            this.typeElementSimpleName = null;
        }
        this.getterSimpleName = getterSimpleName;
    }

    private static String sanitizeTypeName(String typeName) {
        String sanitized = typeName = typeName.substring(1 + typeName.lastIndexOf(' '));
        if (sanitized.endsWith(")")) {
            sanitized = sanitized.substring(0, typeName.length() - 1);
        }
        return sanitized;
    }
}
