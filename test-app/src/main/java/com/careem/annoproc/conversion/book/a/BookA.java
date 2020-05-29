package com.careem.annoproc.conversion.book.a;

import com.careem.annoproc.converter.annotation.Converter;
import com.careem.annoproc.conversion.book.b.BookB;
import lombok.Builder;

public class BookA {
    String bookName;

    @Converter(sourceClass = BookB.class)
    @Builder
    public BookA(String bookName) {
        this.bookName = bookName;
    }
}
