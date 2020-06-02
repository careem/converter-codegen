package com.careem.annotation.processing.converter.generator.mapping;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Data class for mapping result
 */
@Getter
public class ConversionMappingResult {
    private final List<Import> imports;
    private final List<Var> vars;

    /**
     * The resulting expression for type conversion.
     * For example, consider {@code
     * @Getter
     * @Builder
     * class A {
     *     List<String> values;
     * }
     *
     * @Builder
     * class B {
     *     List<Integer> values;
     * }}.
     * Then, to convert instance {@code from} of A
     * to instance of B, we will be doing as follows:
     * {@code B.builder().values(
     *     from.getValues().stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()))
     *     .build()}
     * then {@code from.getValues().stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList())}
     * will be the {@code expression}
     */
    private final String expression;

    public ConversionMappingResult(String expression) {
        this.expression = expression;
        this.imports = Collections.emptyList();
        this.vars = Collections.emptyList();
    }

    public ConversionMappingResult(List<Import> imports, List<Var> vars, String expression) {
        this.imports = Collections.unmodifiableList(imports);
        this.vars = Collections.unmodifiableList(vars);
        this.expression = expression;
    }

    @EqualsAndHashCode
    public static class Import {
        private final String path;
        private final String prefix;

        public Import(String path) {
            this(path, false);
        }

        public Import(String path, boolean isStatic) {
            this.path = path;
            if (isStatic) {
                this.prefix = "import static ";
            } else {
                this.prefix = "import ";
            }
        }

        public String toImportStatement() {
            return prefix + path + ";";
        }
    }

    @EqualsAndHashCode
    @Getter
    public static class Var implements Comparable<Var> {
        private final String name;
        private final String classSimpleName;
        private final boolean isLazy;

        public Var(String name, String classSimpleName) {
            this.name = name;
            this.classSimpleName = classSimpleName;
            this.isLazy = false;
        }

        public Var(String name, String classSimpleName, boolean isLazy) {
            this.name = name;
            this.classSimpleName = classSimpleName;
            this.isLazy = isLazy;
        }

        public String toThisRef() {
            return "this." + name;
        }

        public String toDeclaration() {
            return "private final " + this.classSimpleName + " " + this.name + ";";
        }

        public String toConstructorParam() {
            return (isLazy ? "@Lazy " : "") + classSimpleName + " " + name;
        }

        public String toConstructorFieldAssignment() {
            return "this." + name + " = " + name + ";";
        }

        @Override
        public int compareTo(Var o) {
            return this.name.compareTo(o.name);
        }
    }
}
