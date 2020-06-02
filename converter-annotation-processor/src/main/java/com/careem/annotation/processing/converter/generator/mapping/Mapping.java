package com.careem.annotation.processing.converter.generator.mapping;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import lombok.NonNull;

public interface Mapping {
    ConversionMappingResult getFor(@NonNull BasicEnclosedElementInfo fromVar, @NonNull BasicEnclosedElementInfo toVar, @NonNull String value);
}
