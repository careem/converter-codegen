package com.careem.annotation.processing.converter.generator.mapper.preset;

import com.careem.annotation.processing.converter.generator.info.BasicEnclosedElementInfo;
import com.careem.annotation.processing.converter.generator.mapper.Mapper;
import com.careem.annotation.processing.converter.generator.mapping.ConversionMappingResult;
import com.careem.annotation.processing.converter.generator.mapping.Mapping;
import com.careem.annotation.processing.converter.util.Types;
import com.careem.annotation.processing.helper.generator.Constants;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.careem.annotation.processing.converter.util.Types.COLLECTORS;
import static com.careem.annotation.processing.converter.util.Types.LIST;
import static com.careem.annotation.processing.converter.util.Types.MAP;
import static com.careem.annotation.processing.converter.util.Types.SET;

/**
 * Main class for getting fromVar to toVar field mapping expression
 */
@NoArgsConstructor
public class DefaultMapper implements Mapper {
    private static final String DOT_STREAM = ".stream()";
    private static final List<Mapping> MAPPINGS = Arrays.asList(
        new BasicMapping(),
        new ConvertersMapping(),
        new GsonMapping());

    @Override
    public ConversionMappingResult getFor(
        int indents,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        return getFor(indents, 0, fromVar, toVar, expr);
    }

    private ConversionMappingResult getFor(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        return getMappingResultFor(indents + 1, level + 1, fromVar, toVar, expr);
    }

    private ConversionMappingResult getMappingResultFor(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        ConversionMappingResult result = getForSameTypes(indents, level, fromVar, toVar, expr);
        if (result != null) {
            return result;
        }

        for (Mapping mapping : MAPPINGS) {
            result = mapping.getFor(fromVar, toVar, expr);
            if (result != null) {
                return result;
            }
        }

        return result;
    }

    private ConversionMappingResult getForSameTypes(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        if (fromVar.getTypeName().equals(toVar.getTypeName())) {
            return new ConversionMappingResult(expr);
        }

        if (fromVar.getTypeElementName().equals(toVar.getTypeElementName())) {
            if (fromVar.getTypeElementName().equals(LIST)) {
                return getForListToListMapping(indents, level, fromVar, toVar, expr);
            } else if (fromVar.getTypeElementName().equals(MAP)) {
                return getForMapToMapMapping(indents, level, fromVar, toVar, expr);
            } else if (fromVar.getTypeElementName().equals(SET)) {
                return getForSetToSetMapping(indents, level, fromVar, toVar, expr);
            }
        }

        return null;
    }

    private ConversionMappingResult getForListToListMapping(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        final String varName = "o" + level;
        if (fromVar.getTypeElementName().equals(LIST)) {
            final ConversionMappingResult result
                = getForTypeArguments(indents, level, 0, fromVar, toVar, varName);

            if (result == null) {
                return null;
            }

            // TODO: optimize
            final List<ConversionMappingResult.Import> imports = new ArrayList<>();
            imports.addAll(result.getImports());
            imports.add(new ConversionMappingResult.Import(COLLECTORS));
            final String prefix = Constants.LINE_SEPARATOR + Constants.INDENTS_MAP.get(indents);

            return new ConversionMappingResult(imports, result.getVars(), expr +
                prefix + DOT_STREAM +
                prefix + ".map(" + varName + " -> " + result.getExpression() + ")" +
                prefix + ".collect(Collectors.toList())");
        }
        return null;
    }

    private ConversionMappingResult getForMapToMapMapping(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        final String varName1 = "o" + level;
        final String varName2 = "t" + level;
        if (fromVar.getTypeElementName().equals(Types.MAP)) {
            final ConversionMappingResult result1
                = getForTypeArguments(indents, level, 0, fromVar, toVar, varName1 + ".getKey()");

            if (result1 == null) {
                return null;
            }

            final ConversionMappingResult result2
                = getForTypeArguments(indents, level, 1, fromVar, toVar, varName2 + ".getValue()");

            if (result2 == null) {
                return null;
            }

            // TODO: optimize
            final List<ConversionMappingResult.Import> imports = new ArrayList<>();
            imports.addAll(result1.getImports());
            imports.addAll(result2.getImports());
            imports.add(new ConversionMappingResult.Import(COLLECTORS));

            final List<ConversionMappingResult.Var> vars = new ArrayList<>();
            vars.addAll(result1.getVars());
            vars.addAll(result2.getVars());

            final String prefix = Constants.LINE_SEPARATOR + Constants.INDENTS_MAP.get(indents);
            return new ConversionMappingResult(imports, vars, expr +
                prefix + ".entrySet()" +
                prefix + DOT_STREAM +
                prefix + ".collect(" +
                prefix + "    Collectors.toMap(" +
                prefix + "        " + varName1 + " -> " + result1.getExpression() + ", " +
                prefix + "        " + varName2 + " -> " + result2.getExpression() + "))");
        }
        return null;
    }

    private ConversionMappingResult getForSetToSetMapping(
        int indents,
        int level,
        BasicEnclosedElementInfo fromVar,
        BasicEnclosedElementInfo toVar,
        String expr) {

        final String varName = "o" + level;
        if (fromVar.getTypeElementName().equals(SET)) {
            final ConversionMappingResult result
                = getForTypeArguments(indents, level, 0, fromVar, toVar, varName);

            if (result == null) {
                return null;
            }

            // TODO: optimize
            final List<ConversionMappingResult.Import> imports = new ArrayList<>();
            imports.addAll(result.getImports());
            imports.add(new ConversionMappingResult.Import(COLLECTORS));

            final String prefix = Constants.LINE_SEPARATOR + Constants.INDENTS_MAP.get(indents);
            return new ConversionMappingResult(imports, result.getVars(), expr +
                prefix + DOT_STREAM +
                prefix + ".map(" + varName + " -> " + result.getExpression() + ")" +
                prefix + ".collect(Collectors.toSet())");
        }
        return null;
    }

    private ConversionMappingResult getForTypeArguments(
        int indents,
        int level,
        int index,
        BasicEnclosedElementInfo from,
        BasicEnclosedElementInfo to,
        String expr) {

        return getFor(
            indents,
            level,
            new BasicEnclosedElementInfo(from.getType().getTypeArguments().get(index), null),
            new BasicEnclosedElementInfo(to.getType().getTypeArguments().get(index), null),
            expr);
    }
}
