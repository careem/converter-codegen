package com.careem.annoproc.converter.generator;

import com.careem.annoproc.converter.annotation.Converter;
import com.careem.annoproc.converter.generator.info.ClassInfo;
import com.careem.annoproc.converter.generator.mapper.Mapper;
import com.careem.annoproc.converter.generator.state.BuilderConverterClassState;
import com.careem.annoproc.converter.generator.state.visitor.BuilderConverterClassStateVisitor;
import com.careem.annoproc.converter.template.TemplateContentBuilder;
import com.careem.annoproc.converter.util.ConverterAnnotationUtil;
import com.careem.annoproc.helper.generator.AbstractGenerator;
import com.careem.annoproc.helper.generator.util.WriterProvider;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.careem.annoproc.converter.util.Util.getConverters;

/**
 * Generates converter source files
 */
public class ConverterGenerator extends AbstractGenerator {
    private final List<Mapper> mappers;

    public ConverterGenerator(ProcessingEnvironment processingEnv, WriterProvider writerProvider, List<Mapper> mappers) {
        super(processingEnv, writerProvider);
        this.mappers = mappers;
    }

    public void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws IOException {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() == ElementKind.CLASS) {
                    processElement(element, getConverters((TypeElement) element));
                } else if (element.getKind() == ElementKind.CONSTRUCTOR) {
                    processElement(element, getConverters((ExecutableElement) element));
                }
            }
        }
    }

    private void processElement(Element element, Converter[] converters) throws IOException {
        for (Converter converter : converters) {
            if (converter.assumeExists()) {
                continue;
            }

            try {
                final TypeElement sourceClassElement = ConverterAnnotationUtil.getTypeElementForSourceClass(converter);
                if (!sourceClassElement.getQualifiedName().toString().equals(Void.class.getCanonicalName())) {
                    generateCode(element, sourceClassElement, converter);
                    continue;
                }

                final TypeElement otherTargetElement = ConverterAnnotationUtil.getTypeElementForTargetClass(converter);
                if (!otherTargetElement.getQualifiedName().toString().equals(Void.class.getCanonicalName())){
                    generateCode(otherTargetElement, (TypeElement)element, converter);
                    continue;
                }

                throw new IllegalArgumentException(
                    "neither sourceClass nor targetClass found for Converter on " + element.getSimpleName().toString());
            } finally {
                closeSourceFile();
            }
        }
    }
    /**
     * Entry point for single source file generation
     * @param targetElement
     * @param sourceClassElement
     * @param converter
     * @throws IOException
     */
    private void generateCode(Element targetElement, TypeElement sourceClassElement, Converter converter)
        throws IOException {

        final ClassInfo targetClass = getTargetClassInfo(targetElement, converter);
        final ClassInfo sourceClass = new ClassInfo(sourceClassElement, true);

        final BuilderConverterClassState state = new BuilderConverterClassState(targetClass, sourceClass, converter);

        state.accept(new BuilderConverterClassStateVisitor(mappers));

        final String content = new TemplateContentBuilder(state).build();

        createAndOpenSourceFile(state.getConverterClass().getName(), content);
    }

    private ClassInfo getTargetClassInfo(Element targetElement, Converter converter) {
        if (targetElement.getKind() == ElementKind.CLASS) {
            return new ClassInfo((TypeElement)targetElement, converter);
        } else if (targetElement.getKind() == ElementKind.CONSTRUCTOR) {
            return new ClassInfo((ExecutableElement) targetElement, converter);
        } else {
            throw new IllegalArgumentException("Unsupported element kind");
        }
    }
}
