package com.careem.annoproc.conversion;

import com.careem.annoproc.conversion.book.a.BookA;
import com.careem.annoproc.conversion.book.b.BookB;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookAToBookBCustomConverter implements Converter<BookA, BookB> {

    @Override
    public BookB convert(BookA bookA) {
        return null;
    }
}
