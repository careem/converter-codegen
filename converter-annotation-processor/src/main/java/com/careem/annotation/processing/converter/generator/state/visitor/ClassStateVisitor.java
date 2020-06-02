package com.careem.annotation.processing.converter.generator.state.visitor;

import com.careem.annotation.processing.converter.generator.state.ClassState;

/**
 * Interface for ClassState visitor classes
 */
public interface ClassStateVisitor<T extends ClassState> {
    void visit(T state);
}
