package com.careem.annoproc.converter.generator.mapper.custom;

import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResultUtil;
import com.careem.annoproc.converter.generator.mapping.Mapping;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This mapping uses conversionService.convert
 * instead of specific converter.convert
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversionServiceConverterMapping implements Mapping {
    private static final ConversionServiceConverterMapping instance = new ConversionServiceConverterMapping();

    static ConversionServiceConverterMapping getInstance() {
        return instance;
    }

    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String value) {
        return ConversionMappingResultUtil.createGenericConversionMappingResult(toVar, value);
    }
}
