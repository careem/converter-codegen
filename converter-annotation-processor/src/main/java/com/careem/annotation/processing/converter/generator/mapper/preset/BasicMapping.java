package com.careem.annotation.processing.converter.generator.mapper.preset;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.converter.generator.mapping.CircumfixMapping;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.mapping.EqualityMapping;
import com.careem.annotation.processing.converter.generator.mapping.Mapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.careem.annotation.processing.converter.util.Types.Primitive.BYTE;
import static com.careem.annotation.processing.converter.util.Types.Primitive.DOUBLE;
import static com.careem.annotation.processing.converter.util.Types.Primitive.FLOAT;
import static com.careem.annotation.processing.converter.util.Types.Primitive.INT;
import static com.careem.annotation.processing.converter.util.Types.Primitive.LONG;
import static com.careem.annotation.processing.converter.util.Types.Primitive.SHORT;

class BasicMapping implements Mapping {
    private final Map<String, Map<String, Mapping>> mappings;

    public BasicMapping() {
        this.mappings = createMappings();
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String expr) {
        final Map<String, Mapping> map = mappings.get(fromVar.getTypeName());
        if (map == null) {
            return null;
        }

        final Mapping conv = map.get(toVar.getTypeName());
        if (conv == null) {
            return null;
        }

        return conv.getFor(fromVar, toVar, expr);
    }

    private static Map<String, Map<String, Mapping>> createMappings() {
        final Map<String, Map<String, Mapping>> map = new HashMap<>();
        map.put(Byte.class.getName(), getSuffixMap(BYTE));
        map.put(Short.class.getName(), getSuffixMap(SHORT));
        map.put(Integer.class.getName(), getSuffixMap(INT));
        map.put(Long.class.getName(), getSuffixMap(LONG));
        map.put(Float.class.getName(), getSuffixMap(FLOAT));
        map.put(Double.class.getName(), getSuffixMap(DOUBLE));
        map.put(String.class.getName(), getStringSuffixMap());
        map.put(BYTE, getEqualityMappingMap(Byte.class.getName()));
        map.put(SHORT, getEqualityMappingMap(Short.class.getName()));
        map.put(INT, getEqualityMappingMap(Integer.class.getName()));
        map.put(LONG, getEqualityMappingMap(Long.class.getName()));
        map.put(FLOAT, getEqualityMappingMap(Float.class.getName()));
        map.put(DOUBLE, getEqualityMappingMap(Double.class.getName()));
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, Mapping> getSuffixMap(String typeNameToOverride) {
        final Map<String, Mapping> map = new HashMap<>();
        map.put(Byte.class.getName(), convSuffix(".byteValue()"));
        map.put(Short.class.getName(), convSuffix(".shortValue()"));
        map.put(Integer.class.getName(), convSuffix(".intValue()"));
        map.put(Long.class.getName(), convSuffix(".longValue()"));
        map.put(Float.class.getName(), convSuffix(".floatValue()"));
        map.put(Double.class.getName(), convSuffix(".doubleValue()"));
        map.put(String.class.getName(), conv("String.valueOf(", ")"));

        map.put(BYTE, convSuffix(".byteValue()"));
        map.put(SHORT, convSuffix(".shortValue()"));
        map.put(INT, convSuffix(".intValue()"));
        map.put(LONG, convSuffix(".longValue()"));
        map.put(FLOAT, convSuffix(".floatValue()"));
        map.put(DOUBLE, convSuffix(".doubleValue()"));

        // override with equalityMapping
        if (typeNameToOverride != null) {
            map.put(typeNameToOverride, EqualityMapping.getInstance());
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, Mapping> getEqualityMappingMap(String typeName) {
        final Mapping equalityMapping = EqualityMapping.getInstance();
        final Map<String, Mapping> map = new HashMap<>();
        map.put(typeName, equalityMapping);
        return map;
    }

    private static Map<String, Mapping> getStringSuffixMap() {
        final Map<String, Mapping> map = new HashMap<>();
        map.put(Byte.class.getName(), conv("Byte.valueOf(", ")"));
        map.put(Short.class.getName(), conv("Short.valueOf(", ")"));
        map.put(Integer.class.getName(), conv("Integer.valueOf(", ")"));
        map.put(Long.class.getName(), conv("Long.valueOf(", ")"));
        map.put(Float.class.getName(), conv("Float.valueOf(", ")"));
        map.put(Double.class.getName(), conv("Double.valueOf(", ")"));
        map.put(String.class.getName(), conv("String.valueOf(", ")"));
        return Collections.unmodifiableMap(map);
    }

    private static CircumfixMapping conv(String prefix, String suffix) {
        return new CircumfixMapping(prefix, suffix);
    }

    private static CircumfixMapping convSuffix(String suffix) {
        return new CircumfixMapping(null, suffix);
    }
}
