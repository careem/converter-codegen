package com.careem.testapp.conversion.author.b;

import com.careem.testapp.conversion.author.Author;
import com.careem.testapp.conversion.author.a.AuthorA;
import com.careem.testapp.conversion.author.c.AuthorC;
import com.careem.testapp.conversion.book.b.BookB;
import com.careem.annotation.processing.converter.annotation.Converter;
import com.careem.annotation.processing.converter.annotation.ConverterMapping;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Converter(
    sourceClass = AuthorA.class
)
public class AuthorB extends Author {
    @Builder
    public AuthorB(String name, String str, Integer integer, Float aFloat, Double aDouble, String integerStr, String authorC, List<String> authorCList1, List<AuthorC> authorCList2, List<Integer> integerList, Set<Integer> aSet1, Set<String> aSet2, Map<Integer, String> aMap1, Map<String, String> aMap2, Map<String, Integer> aMap3, Map<Integer, Integer> aMap4, List<Map<Set<String>, Integer>> aListOfMapOfSetKey, Set<Map<List<String>, Integer>> aSetOfMapOfListKey, BookB book) {
        super(name);
        this.str = str;
        this.integer = integer;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.integerStr = integerStr;
        this.authorC = authorC;
        this.authorCList1 = authorCList1;
        this.authorCList2 = authorCList2;
        this.integerList = integerList;
        this.aSet1 = aSet1;
        this.aSet2 = aSet2;
        this.aMap1 = aMap1;
        this.aMap2 = aMap2;
        this.aMap3 = aMap3;
        this.aMap4 = aMap4;
        this.aListOfMapOfSetKey = aListOfMapOfSetKey;
        this.aSetOfMapOfListKey = aSetOfMapOfListKey;
        this.book = book;
    }

    String str;
    Integer integer;
    Float aFloat;
    Double aDouble;

    String integerStr;

    String authorC;

    List<String> authorCList1;
    List<AuthorC> authorCList2;

    @ConverterMapping(sourceClass = AuthorA.class, field = "integerStrList")
    List<Integer> integerList;

    Set<Integer> aSet1;
    Set<String> aSet2;

    Map<Integer, String> aMap1;
    Map<String, String> aMap2;
    Map<String, Integer> aMap3;
    Map<Integer, Integer> aMap4;

    List<Map<Set<String>, Integer>> aListOfMapOfSetKey;
    Set<Map<List<String>, Integer>> aSetOfMapOfListKey;

    BookB book;

    public String getType() {
        return "1";
    }
}
