package com.careem.annoproc.converter.generator.mapper.custom;

import com.careem.annoproc.converter.generator.mapping.Mapping;
import lombok.AllArgsConstructor;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Helper class for finding the mapping based on
 * {@link com.careem.annoproc.converter.annotation.ConverterConfiguration} added on interfaces
 */
class InterfacesTracer {
    private static final int INTERFACE_LEVEL_LIMIT = 2;

    private List<TypeMirror> moreInterfaces;
    private Function<TypeMirror, Mapping> iteratee;

    InterfacesTracer(List<? extends TypeMirror> initialInterfaces, Function<TypeMirror, Mapping> iteratee) {
        this.moreInterfaces = (List<TypeMirror>)initialInterfaces;
        this.iteratee = iteratee;
    }

    Mapping findMapping() {

        int interfaceLevel = 0;

        while (!this.moreInterfaces.isEmpty()) {
            ++interfaceLevel;

            if (interfaceLevel > INTERFACE_LEVEL_LIMIT) {
                return null;
            }

            TracingResult result = traceInterfaces(this.moreInterfaces);
            if (result.mapping != null) {
                return result.mapping;
            }

            this.moreInterfaces = result.moreInterfaces;
        }

        return null;
    }

    private TracingResult traceInterfaces(List<TypeMirror> interfaces) {
        final List<TypeMirror> newInterfaces = new ArrayList<>();

        for (TypeMirror anInterface : interfaces) {
            final Mapping mapping = iteratee.apply(anInterface);
            if (mapping != null) {
                return new TracingResult(newInterfaces, mapping);
            }

            final TypeElement interfaceTypeElement = ((TypeElement) ((DeclaredType) anInterface).asElement());
            newInterfaces.addAll(interfaceTypeElement.getInterfaces());
        }

        return new TracingResult(newInterfaces, null);
    }

    @AllArgsConstructor
    private static class TracingResult {
        List<TypeMirror> moreInterfaces;
        Mapping mapping;
    }
}
