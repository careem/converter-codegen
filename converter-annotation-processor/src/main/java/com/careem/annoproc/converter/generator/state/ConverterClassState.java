package com.careem.annoproc.converter.generator.state;

import com.careem.annoproc.converter.annotation.Converter;
import com.careem.annoproc.converter.generator.info.BasicClassInfo;
import com.careem.annoproc.converter.generator.info.ClassInfo;
import com.careem.annoproc.converter.generator.info.ConverterClassInfo;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.util.ConverterAnnotationUtil;
import com.careem.annoproc.converter.util.Types;
import lombok.Getter;

import javax.lang.model.element.TypeElement;

/**
 * Manages state of to-be-generated converter class
 */
@Getter
public class ConverterClassState extends ClassState {
    private final ClassInfo targetClass;
    private final ClassInfo sourceClass;
    private final ConverterClassInfo converterClass;

    private final Converter converter;

    public ConverterClassState(ClassInfo targetClass, ClassInfo sourceClass, Converter converter) {
        this.targetClass = targetClass;
        this.sourceClass = sourceClass;
        this.converter = converter;

        final BasicClassInfo interfaceTargetClass = getInterfaceTargetClassInfo(converter);

        if (interfaceTargetClass.getName().equals(Types.VOID)) {
            this.converterClass = new ConverterClassInfo(sourceClass, targetClass, converter.nonNull());
        } else {
            this.converterClass
                = new ConverterClassInfo(sourceClass, targetClass, interfaceTargetClass, converter.nonNull());

            if (!interfaceTargetClass.getPackageName().equals(converterClass.getPackageName())) {
                imports.add(new ConversionMappingResult.Import(interfaceTargetClass.getName()));
            }
        }

        if (!targetClass.getPackageName().equals(converterClass.getPackageName())) {
            imports.add(new ConversionMappingResult.Import(targetClass.getName()));
        }

        if (!sourceClass.getPackageName().equals(converterClass.getPackageName())) {
            imports.add(new ConversionMappingResult.Import(sourceClass.getName()));
        }

        for (Converter.Import anImport : targetClass.getImports()) {
            imports.add(new ConversionMappingResult.Import(anImport.value(), anImport.isStatic()));
        }
    }

    private BasicClassInfo getInterfaceTargetClassInfo(Converter converter) {
        TypeElement typeElement = ConverterAnnotationUtil.getTypeElementForInterfaceTargetClass(converter);
        return new ClassInfo(typeElement, false);
    }
}
