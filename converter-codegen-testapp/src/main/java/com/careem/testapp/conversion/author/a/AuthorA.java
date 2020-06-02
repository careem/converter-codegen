package com.careem.testapp.conversion.author.a;

import com.careem.testapp.conversion.author.b.AuthorB;
import com.careem.testapp.conversion.author.c.AuthorC;
import com.careem.testapp.conversion.book.a.BookA;
import com.careem.annotation.processing.converter.annotation.Converter;
import com.careem.annotation.processing.converter.annotation.ConverterMapping;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ToString
@EqualsAndHashCode
@Converter(
    sourceClass = AuthorB.class,
    nonNull = false
)
@Builder
@Getter
public class AuthorA {
    String str;
    Integer integer;
    Float aFloat;
    Double aDouble;

    Integer integerStr;

    AuthorC authorC;

    List<AuthorC> authorCList1;
    List<AuthorC> authorCList2;

    @ConverterMapping(sourceClass = AuthorB.class, field = "integerList")
    List<String> integerStrList;

    Set<Integer> aSet1;
    Set<Integer> aSet2;

    Map<Integer, String> aMap1;
    Map<Integer, String> aMap2;
    Map<Integer, String> aMap3;
    Map<Integer, String> aMap4;

    List<Map<Set<Integer>, String>> aListOfMapOfSetKey;
    Set<Map<List<Integer>, String>> aSetOfMapOfListKey;

    BookA book;

    @ConverterMapping(method = "getType")
    Integer type;

    @ConverterMapping(sourceClass = AuthorB.class, value= "1234")
    Integer valueAsItIs;
}
