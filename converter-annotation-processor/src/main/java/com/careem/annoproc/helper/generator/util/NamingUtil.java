package com.careem.annoproc.helper.generator.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.lang.model.element.Name;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NamingUtil {
    public static String pascalCase(@NonNull String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("length is 0");
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String camelCase(@NonNull String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("length is 0");
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static String getterCallName(@NonNull String prefix, @NonNull String fieldName) {
        return prefix + "get" + pascalCase(fieldName) + "()";
    }
}
