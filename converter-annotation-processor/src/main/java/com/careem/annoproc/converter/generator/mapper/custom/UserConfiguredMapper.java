package com.careem.annoproc.converter.generator.mapper.custom;

import com.careem.annoproc.converter.annotation.ConverterConfiguration;
import com.careem.annoproc.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annoproc.converter.generator.mapper.Mapper;
import com.careem.annoproc.converter.generator.mapping.CircumfixMapping;
import com.careem.annoproc.converter.generator.mapping.ClassNameCircumfixMapping;
import com.careem.annoproc.converter.generator.mapping.ConversionMappingResult;
import com.careem.annoproc.converter.generator.mapping.Mapping;
import com.careem.annoproc.converter.util.ConverterAnnotationUtil;
import com.google.common.reflect.ClassPath;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.careem.annoproc.converter.util.BasicExpressionUtil.methodCall;
import static com.careem.annoproc.converter.util.Util.asTypeElement;

public class UserConfiguredMapper implements Mapper {
    private final Map<String, Map<String, Mapping>> mappings = new HashMap<>();
    private final ProcessingEnvironment processingEnvironment;
    private final RoundEnvironment roundEnv;

    public UserConfiguredMapper(
        ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv) throws IOException {
        this.processingEnvironment = processingEnvironment;
        this.roundEnv = roundEnv;

        initMappings();
        initConverters();
    }

    @SuppressWarnings("fb-contrib:STT_TOSTRING_STORED_IN_FIELD")
    @Override
    public ConversionMappingResult getFor(int indents, BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String expr) {
        Map<String, Mapping> map = mappings.get(fromVar.getTypeName());
        if (map != null) {
            Mapping mapping = map.get(toVar.getTypeName());
            if (mapping != null) {
                return mapping.getFor(fromVar, toVar, expr);
            }
            if (toVar.getTypeElement() != null) {
                for (TypeMirror anInterface : toVar.getTypeElement().getInterfaces()) {
                    final String name = ((TypeElement) ((DeclaredType) anInterface).asElement()).getQualifiedName().toString();
                    mapping = map.get(name);
                    if (mapping != null) {
                        return mapping.getFor(fromVar, toVar, expr);
                    }
                }
            }
        }

        return getForInterface(fromVar, toVar, expr);
    }

    private ConversionMappingResult getForInterface(BasicEnclosedElementInfo fromVar, BasicEnclosedElementInfo toVar, String expr) {
        if (fromVar.getTypeElement() == null) {
            return null;
        }

        final List<? extends TypeMirror> interfaces = fromVar.getTypeElement().getInterfaces();
        final Mapping mapping = new InterfacesTracer(interfaces, anInterface -> {

            final String name = ((TypeElement) ((DeclaredType) anInterface).asElement()).getQualifiedName().toString();
            final Map<String, Mapping> map = mappings.get(name);
            if (map != null) {
                return map.get(toVar.getTypeName());
            }

            return null;
        }).findMapping();

        if (mapping != null) {
            return mapping.getFor(fromVar, toVar, expr);
        }

        return null;
    }

    /**
     * User configured converters mappings
     */
    private void initMappings() throws IOException {
        for (Element element : this.roundEnv.getElementsAnnotatedWith(ConverterConfiguration.class)) {
            for (ConverterConfiguration configuration : element.getAnnotationsByType(ConverterConfiguration.class)) {
                for (ConverterConfiguration.Mapping mappingConfig : configuration.mappings()) {
                    final TypeElement from = ConverterAnnotationUtil.getFromTypeElement(mappingConfig);
                    final TypeElement to = ConverterAnnotationUtil.getToTypeElement(mappingConfig);

                    final Mapping mapping = getMapping(mappingConfig, from, to);

                    this.mappings.computeIfAbsent(
                        from.getQualifiedName().toString(), s -> new HashMap<>())
                        .put(to.getQualifiedName().toString(), mapping);
                }

                for (String aPackage : configuration.packages()) {
                    final ClassPath classPath = ClassPath.from(UserConfiguredMapper.class.getClassLoader());

                    for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(aPackage)) {
                        TypeElement typeElement
                            = processingEnvironment.getElementUtils().getTypeElement(classInfo.getName());
                        addIfConverter(this.mappings, typeElement);
                    }
                }
            }
        }
    }

    private static Mapping getMapping(ConverterConfiguration.Mapping mappingConfig, TypeElement from, TypeElement to) {
        switch (mappingConfig.strategy()) {
            case CIRCUMFIX:
                return new CircumfixMapping(
                    mappingConfig.prefix(),
                    mappingConfig.suffix(),
                    Arrays.asList(
                        new ConversionMappingResult.Import(from.getQualifiedName().toString()),
                        new ConversionMappingResult.Import(to.getQualifiedName().toString())),
                    Collections.emptyList());
            case CLASS_NAME_CIRCUMFIX:
                return new ClassNameCircumfixMapping(
                    mappingConfig.prefix(),
                    mappingConfig.suffix(),
                    Arrays.asList(
                        new ConversionMappingResult.Import(from.getQualifiedName().toString()),
                        new ConversionMappingResult.Import(to.getQualifiedName().toString())),
                    Collections.emptyList());
            default:
                throw new IllegalArgumentException("Unknown strategy");
        }
    }

    /**
     * User defined converters mapping
     */
    private void initConverters() {
        for (Element rootElement : this.roundEnv.getRootElements()) {
            if (rootElement.getKind() != ElementKind.CLASS) {
                continue;
            }

            final TypeElement typeElement = (TypeElement) rootElement;
            addIfConverter(this.mappings, typeElement);
        }
    }

    private static void addIfConverter(Map<String, Map<String, Mapping>> maps, TypeElement typeElement) {
        for (TypeMirror anInterface : typeElement.getInterfaces()) {
            if (!anInterface.toString().startsWith("org.springframework.core.convert.converter.Converter<")) {
                continue;
            }

            final DeclaredType declaredType = (DeclaredType) anInterface;
            final List<? extends TypeMirror> arguments = declaredType.getTypeArguments();

            final String from = asTypeElement(arguments.get(0)).getQualifiedName().toString();
            final String to = asTypeElement(arguments.get(1)).getQualifiedName().toString();

//            final BasicClassInfo converterClassInfo = new BasicClassInfo(typeElement);
            maps.computeIfAbsent(from, s -> new HashMap<>())
                .put(to, ConversionServiceConverterMapping.getInstance()); //TODO: experimenting for multi-module
//                .put(to, new UserDefinedConverterMapping(
//                    converterClassInfo.getName(),
//                    new ConversionMappingResult.Var(NamingUtil.camelCase(converterClassInfo.getSimpleName()), converterClassInfo.getSimpleName(), true)
//                ));
        }
    }
}
