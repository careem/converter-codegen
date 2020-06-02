package com.careem.annotation.processing.converter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Types {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Primitive {
        public static final String BYTE = "byte";
        public static final String SHORT = "short";
        public static final String INT = "int";
        public static final String LONG = "long";
        public static final String FLOAT = "float";
        public static final String DOUBLE = "double";
    }

    public static final String VOID = Void.class.getName();
    public static final String STRING = String.class.getName();
    public static final String LIST = List.class.getName();
    public static final String MAP = Map.class.getName();
    public static final String SET = Set.class.getName();
    public static final String COLLECTORS = Collectors.class.getName();

    public static final String GSON = "com.google.gson.Gson";
}
