package com.careem.annotation.processing.helper.generator.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class WriterProvider {
    public PrintWriter newPrintWriter(Writer writer) {
        return new PrintWriter(writer);
    }

    public PrintWriter newPrintWriter(OutputStream outputStream) {
        return new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }
}
