package com.careem.annotation.processing.converter.template;

import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.state.BuilderConverterClassState;
import com.careem.annotation.processing.helper.generator.Constants;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Builds content for source file in builder format
 */
public class TemplateContentBuilder {
    private String content;

    private BuilderConverterClassState state;

    public TemplateContentBuilder(@NonNull BuilderConverterClassState state) throws IOException {
        final InputStream stream = getClass().getResourceAsStream("/com/careem/annotation/processing/converter/template/builder.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(Constants.LINE_SEPARATOR);
            }
            content = builder.toString();
        }
        this.state = state;
    }

    public String build() {
        final String imports = state.getImports().stream()
            .map(ConversionMappingResult.Import::toImportStatement)
            .sorted()
            .reduce(new LinesReducer("")::reduce)
            .orElse("");

        final String vars = state.getClassVars().stream()
            .sorted()
            .map(ConversionMappingResult.Var::toDeclaration)
            .reduce(new LinesReducer(Constants.INDENT)::reduce)
            .map(s -> s + "\n").orElse("");

        replace("PACKAGE", state.getConverterClass().getPackageName());
        replace("ADDITIONAL_IMPORTS", imports);

        replace("ADDITIONAL_VARS", vars);

        replace("FROM_CLASS", state.getConverterClass().getFromClass().getSimpleName());
        replace("TO_CLASS", state.getConverterClass().getToClass().getSimpleName());
        replace("INTERFACE_TO_CLASS", state.getConverterClass().getInterfaceToClass().getSimpleName());

        if (state.getConverterClass().isNonNull()) {
            replace("NON_NULL", "@NonNull");
            replace("NULL_CHECK_CODE", "");
        } else {
            replace("NON_NULL", "");
            replace("NULL_CHECK_CODE", "if (from == null) { return null; }");
        }


        replace("CONSTRUCTOR_PARAMS", state.getClassVars()
            .stream()
            .sorted()
            .map(ConversionMappingResult.Var::toConstructorParam)
            .collect(Collectors.joining(", ")));

        final String INDENTS_2 = Constants.INDENTS_MAP.get(2);
        replace("CONSTRUCTOR_CODE", state.getClassVars()
            .stream()
            .sorted()
            .map(ConversionMappingResult.Var::toConstructorFieldAssignment)
            .reduce(new LinesReducer(INDENTS_2)::reduce)
            .orElse(""));

        replace("BUILD_PROPS", state.buildPropsCode());

        return this.content;
    }

    private void replace(String target, String replacement) {
        content = content.replace("{{" + target + "}}", replacement);
    }

    private class LinesReducer {
        private final String prefix;
        public LinesReducer(String prefix) {
            this.prefix = prefix;
        }
        public String reduce(String p, String c) {
            return p + Constants.LINE_SEPARATOR + prefix + c;
        }
    }
}
