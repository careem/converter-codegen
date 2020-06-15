package com.careem.annotation.processing.converter;

import com.careem.annotation.processing.converter.generator.ConverterGenerator;
import com.careem.annotation.processing.converter.generator.mapper.custom.UserConfiguredMapper;
import com.careem.annotation.processing.converter.generator.mapper.preset.DefaultMapper;
import com.careem.annotation.processing.helper.generator.util.WriterProvider;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Annotation processor class for generating converters
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "com.careem.annotation.processing.converter.annotation.Converter",
    "com.careem.annotation.processing.converter.annotation.Converters"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ConverterAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final WriterProvider writerProvider = new WriterProvider();

        try {
            new ConverterGenerator(
                processingEnv,
                writerProvider,
                Arrays.asList(new UserConfiguredMapper(processingEnv, roundEnv), new DefaultMapper())
            ).process(annotations, roundEnv);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
