package com.careem.annoproc.converter.generator.mapping;

import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EqualityMapping implements Mapping {
    private static final Mapping instance = new EqualityMapping();

    public static Mapping getInstance() {
        return instance;
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String value) {
        return new ConversionMappingResult(value);
    }
}
