package com.careem.testapp.conversion.book.a;

import com.careem.annotation.processing.converter.annotation.Converter;
import com.careem.testapp.conversion.book.b.BookB;
import lombok.Builder;

public class BookA {
    String bookName;

    @Converter(sourceClass = BookB.class)
    @Builder
    public BookA(String bookName) {
        this.bookName = bookName;
    }
}
