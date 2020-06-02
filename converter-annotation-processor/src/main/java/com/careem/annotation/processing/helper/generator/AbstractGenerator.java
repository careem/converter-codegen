package com.careem.annotation.processing.helper.generator;

import com.careem.annotation.processing.helper.generator.util.WriterProvider;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractGenerator {

    protected final ProcessingEnvironment processingEnv;
    private final WriterProvider writerProvider;

    private PrintWriter out = null;

    public AbstractGenerator(ProcessingEnvironment processingEnv, WriterProvider writerProvider) {
        this.processingEnv = processingEnv;
        this.writerProvider = writerProvider;
    }

    protected void createAndOpenSourceFile(String name, String content) throws IOException {
        final JavaFileObject fileObject = this.processingEnv.getFiler().createSourceFile(name);
        this.out = writerProvider.newPrintWriter(fileObject.openWriter());
        if (content != null) {
            this.out.write(content);
        }
    }

    protected void closeSourceFile() {
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
    }

    protected void ln(String str) {
        out.println(str);
    }
}
