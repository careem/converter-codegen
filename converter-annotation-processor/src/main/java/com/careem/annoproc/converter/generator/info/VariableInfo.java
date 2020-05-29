package com.careem.annoproc.converter.generator.info;

import com.careem.annoproc.helper.generator.util.NamingUtil;
import lombok.Getter;

import javax.lang.model.element.VariableElement;

/**
 * Data class for keeping variable information
 */
@Getter
public class VariableInfo extends BasicEnclosedElementInfo {
    private final VariableElement element;
    private final String simpleName;

    @SuppressWarnings("fb-contrib:STT_TOSTRING_STORED_IN_FIELD")
    public VariableInfo(VariableElement variableElement) {
        super(variableElement.asType(), NamingUtil.getterCallName("", variableElement.getSimpleName().toString()));
        this.simpleName = variableElement.getSimpleName().toString();
        this.element = variableElement;
    }
}
