package com.careem.testapp.conversion;

import com.careem.testapp.conversion.book.a.BookA;
import com.careem.testapp.conversion.book.b.BookB;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookAToBookBCustomConverter implements Converter<BookA, BookB> {

    @Override
    public BookB convert(BookA bookA) {
        return null;
    }
}
