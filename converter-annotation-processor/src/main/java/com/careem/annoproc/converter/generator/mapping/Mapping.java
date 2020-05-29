package com.careem.annoproc.converter.generator.mapping;

import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import lombok.NonNull;

public interface Mapping {
    ConversionMappingResult getFor(@NonNull BasicEnclosedElementInfo fromVar, @NonNull BasicEnclosedElementInfo toVar, @NonNull String value);
}
