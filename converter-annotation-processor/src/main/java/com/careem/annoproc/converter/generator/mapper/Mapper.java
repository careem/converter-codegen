package com.careem.annoproc.converter.generator.mapper;

import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import lombok.NonNull;

public interface Mapper {
    ConversionMappingResult getFor(int indents, @NonNull BasicEnclosedElementInfo fromVar, @NonNull BasicEnclosedElementInfo toVar, String expr);
}
