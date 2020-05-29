package com.careem.annoproc.helper.generator;

import com.careem.annoproc.helper.generator.util.WriterProvider;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
