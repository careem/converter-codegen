package com.careem.annoproc.converter.generator.mapper.preset;

import com.careem.annoproc.converter.annotation.Converter;
import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.generator.mapping.Mapping;
import com.careem.annoproc.converter.util.ConverterAnnotationUtil;
import lombok.NoArgsConstructor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static com.careem.annoproc.converter.generator.mapping.ConversionMappingResultUtil.createGenericConversionMappingResult;

@NoArgsConstructor
class ConvertersMapping implements Mapping {
    @Override
    public ConversionMappingResult getFor(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String expr) {
        ConversionMappingResult result
            = getConversionMapping(fromVar, toVar, expr, toVar.getTypeElement().getAnnotationsByType(Converter.class));

        if (result != null) {
            return result;
        }

        for (ExecutableElement executableElement
            : ElementFilter.constructorsIn(toVar.getTypeElement().getEnclosedElements())) {
            result = getConversionMapping(
                fromVar,
                toVar,
                expr,
                executableElement.getAnnotationsByType(Converter.class));

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private static ConversionMappingResult getConversionMapping(
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr,
        Converter[] converters) {

        for (Converter converter : converters) {
            final TypeElement typeElement = ConverterAnnotationUtil.getTypeElementForSourceClass(converter);
            if (typeElement.asType().toString().equals(fromVar.getTypeName())) {
                // TODO: do not allow multiple converters for same mapping
                return createGenericConversionMappingResult(toVar, expr);
            }
        }

        return null;
    }
}
