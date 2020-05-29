package com.careem.annoproc.helper.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final Map<Integer, String> INDENTS_MAP;
    public static final String INDENT = "    ";
    static {
        final int max = 10;
        final Map<Integer, String> indentsMap = new HashMap<>(max);
        final StringBuilder builder = new StringBuilder();
        for (int i = 1; i < max; i++) {
            builder.append(INDENT);
            indentsMap.put(i, builder.toString());
        }
        INDENTS_MAP = Collections.unmodifiableMap(indentsMap);
    }
}
