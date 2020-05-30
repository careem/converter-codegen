package com.careem.annoproc.conversion.author;

import com.careem.annoproc.conversion.author.a.AuthorA;
import com.careem.annoproc.conversion.author.a.AuthorBToAuthorAConverter;
import com.careem.annoproc.conversion.author.b.AuthorB;
import com.careem.annoproc.conversion.author.c.AuthorC;
import com.careem.annoproc.conversion.book.a.BookA;
import com.careem.annoproc.conversion.book.b.BookB;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AuthorBToAuthorAConverterTest {
    @Mock
    private ConversionService conversionService;

    private Gson gson = new Gson();
    private BookA mockBookA = Mockito.mock(BookA.class);

    @Test
    public void test() {
        final AuthorB authorB = randomAuthorB();
        doReturn(mockBookA)
            .when(conversionService)
            .convert(authorB.getBook(), BookA.class);

        final AuthorA expected = convert(authorB);
        final AuthorBToAuthorAConverter converter = new AuthorBToAuthorAConverter(conversionService, gson);

        final AuthorA actual = converter.convert(authorB);

        Assert.assertEquals(expected, actual);
    }

    private AuthorA convert(AuthorB from) {
        return AuthorA.builder()
            .aMap4(from.getAMap4()
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        o1 -> o1.getKey(),
                        t1 -> String.valueOf(t1.getValue()))))
            .aMap3(from.getAMap3()
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        o1 -> Integer.valueOf(o1.getKey()),
                        t1 -> String.valueOf(t1.getValue()))))
            .integerStrList(from.getIntegerList()
                .stream()
                .map(o1 -> String.valueOf(o1))
                .collect(Collectors.toList()))
            .valueAsItIs(1234)
            .book(mockBookA)
            .aListOfMapOfSetKey(from.getAListOfMapOfSetKey()
                .stream()
                .map(o1 -> o1
                    .entrySet()
                    .stream()
                    .collect(
                        Collectors.toMap(
                            o2 -> o2.getKey()
                                .stream()
                                .map(o3 -> Integer.valueOf(o3))
                                .collect(Collectors.toSet()),
                            t2 -> String.valueOf(t2.getValue()))))
                .collect(Collectors.toList()))
            .aFloat(from.getAFloat())
            .aDouble(from.getADouble())
            .integer(from.getInteger())
            .type(Integer.valueOf(from.getType()))
            .aMap2(from.getAMap2()
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        o1 -> Integer.valueOf(o1.getKey()),
                        t1 -> t1.getValue())))
            .aMap1(from.getAMap1())
            .aSet1(from.getASet1())
            .aSet2(from.getASet2()
                .stream()
                .map(o1 -> Integer.valueOf(o1))
                .collect(Collectors.toSet()))
            .authorC(this.gson.fromJson(from.getAuthorC(), AuthorC.class))
            .str(from.getStr())
            .aSetOfMapOfListKey(from.getASetOfMapOfListKey()
                .stream()
                .map(o1 -> o1
                    .entrySet()
                    .stream()
                    .collect(
                        Collectors.toMap(
                            o2 -> o2.getKey()
                                .stream()
                                .map(o3 -> Integer.valueOf(o3))
                                .collect(Collectors.toList()),
                            t2 -> String.valueOf(t2.getValue()))))
                .collect(Collectors.toSet()))
            .authorCList2(from.getAuthorCList2())
            .authorCList1(from.getAuthorCList1()
                .stream()
                .map(o1 -> this.gson.fromJson(o1, AuthorC.class))
                .collect(Collectors.toList()))
            .integerStr(Integer.valueOf(from.getIntegerStr()))
            .build();
    }

    private AuthorB randomAuthorB() {
        final Map<Integer, String> aMap1 = new HashMap<>();
        aMap1.put(randomInt(), randomString());
        aMap1.put(randomInt(), randomString());

        final Map<String, String> aMap2 = new HashMap<>();
        aMap2.put(randomIntegerString(), randomString());
        aMap2.put(randomIntegerString(), randomString());

        final Map<String, Integer> aMap3 = new HashMap<>();
        aMap3.put(randomIntegerString(), randomInt());
        aMap3.put(randomIntegerString(), randomInt());

        final Map<Integer, Integer> aMap4 = new HashMap<>();
        aMap4.put(randomInt(), randomInt());
        aMap4.put(randomInt(), randomInt());

        return AuthorB.builder()
            .name(randomString())
            .str(randomString())
            .integer(randomInt())
            .aFloat(randomFloat())
            .aDouble(randomDouble())
            .integerStr(randomIntegerString())
            .authorC("{}")
            .authorCList1(Arrays.asList("{}", "{}"))
            .authorCList2(Arrays.asList(randomAuthorC(), randomAuthorC()))
            .integerList(Arrays.asList(randomInt(), randomInt()))
            .aSet1(new HashSet<>(Arrays.asList(randomInt(), randomInt())))
            .aSet2(new HashSet<>(Arrays.asList(randomIntegerString(), randomIntegerString())))
            .aMap1(aMap1)
            .aMap2(aMap2)
            .aMap3(aMap3)
            .aMap4(aMap4)
            .aListOfMapOfSetKey(Arrays.asList(randomMapOfSetKey(), randomMapOfSetKey()))
            .aSetOfMapOfListKey(new HashSet<>(Arrays.asList(randomMapOfListKey(), randomMapOfListKey())))
            .book(randomBookB())
            .build();
    }

    private Map<Set<String>, Integer> randomMapOfSetKey() {
        final Map<Set<String>, Integer> mapOfSetKey = new HashMap<>();
        mapOfSetKey.put(new HashSet<>(Arrays.asList(randomIntegerString(), randomIntegerString())), randomInt());
        mapOfSetKey.put(new HashSet<>(Arrays.asList(randomIntegerString(), randomIntegerString())), randomInt());
        return mapOfSetKey;
    }

    private Map<List<String>, Integer> randomMapOfListKey() {
        final Map<List<String>, Integer> mapOfListKey = new HashMap<>();
        mapOfListKey.put(Arrays.asList(randomIntegerString(), randomIntegerString()), randomInt());
        mapOfListKey.put(Arrays.asList(randomIntegerString(), randomIntegerString()), randomInt());
        return mapOfListKey;
    }

    private static BookB randomBookB() {
        return BookB.builder()
            .bookName(randomString())
            .build();
    }

    private static AuthorC randomAuthorC() {
        return AuthorC.builder()
            .str(randomString())
            .build();
    }
    private static String randomString() {
        return "str" + Math.random();
    }

    private static String randomIntegerString() {
        return Integer.toString(randomInt());
    }

    private static int randomInt() {
        return new Random().nextInt();
    }

    private static float randomFloat() {
        return new Random().nextFloat();
    }

    private static double randomDouble() {
        return new Random().nextDouble();
    }
}
