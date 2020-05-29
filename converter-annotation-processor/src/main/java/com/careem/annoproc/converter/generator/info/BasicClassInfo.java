package com.careem.annoproc.converter.generator.info;

import lombok.Getter;
import lombok.NonNull;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Data class for keeping common class information
 */
@Getter
public class BasicClassInfo {
    private final String name;
    private final String simpleName;
    private final String packageName;

    public BasicClassInfo(@NonNull String packageName, @NonNull String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.name = packageName + "." + simpleName;
    }

    @SuppressWarnings("fb-contrib:STT_TOSTRING_STORED_IN_FIELD")
    public BasicClassInfo(TypeElement typeElement) {
        this(((PackageElement)typeElement.getEnclosingElement()).getQualifiedName().toString(),
            typeElement.getSimpleName().toString());
    }
}
