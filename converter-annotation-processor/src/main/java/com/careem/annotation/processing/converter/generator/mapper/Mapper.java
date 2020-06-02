package com.careem.annotation.processing.converter.generator.mapper;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import lombok.NonNull;

public interface Mapper {
    ConversionMappingResult getFor(int indents, @NonNull BasicEnclosedElementInfo fromVar, @NonNull BasicEnclosedElementInfo toVar, String expr);
}
