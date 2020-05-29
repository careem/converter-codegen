package com.careem.annoproc.converter.generator.info;

import javax.lang.model.element.ExecutableElement;

/**
 * Data class for keeping method information
 */
public class MethodInfo extends BasicEnclosedElementInfo {
    public MethodInfo(ExecutableElement executableElement) {
        super(executableElement.getReturnType(), executableElement.getSimpleName().toString() + "()");
    }
}
