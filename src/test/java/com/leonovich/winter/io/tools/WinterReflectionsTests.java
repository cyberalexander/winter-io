/*
 * MIT License
 *
 * Copyright (c) 2022 Aliaksandr Leanovich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.leonovich.winter.io.tools;

import com.leonovich.winter.io.testdata.GenericClass;
import com.leonovich.winter.io.testdata.MultiGenericClass;
import com.leonovich.winter.io.testdata.NonGenericClass;
import com.leonovich.winter.io.testdata.StandaloneGenericClass;
import com.leonovich.winter.io.testdata.StandaloneNonGenericClass;
import com.leonovich.winter.io.testdata.TestData;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created : 22/02/2022 09:12
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
class WinterReflectionsTests {
    private final WinterReflections wr = new WinterReflections("com.leonovich.winter.io.testdata");

    @Test
    void testExtractGenericTypeFromNonGenericField() {
        Field nonGenericField = Stream.of(TestData.class.getDeclaredFields())
            .filter(field -> "nonGenericField".equals(field.getName()))
            .findFirst()
            .get();
        log.debug("NonGenericField : {}", nonGenericField);
        String extractedGenericType = wr.extractGenericType(nonGenericField).iterator().next().getTypeName();
        log.debug("Extracted Type : {}", extractedGenericType);

        Assertions.assertEquals(
            String.class.getName(),
            extractedGenericType,
            String.format(
                "Extracted generic type expected to be '%s' but it's '%s'",
                String.class.getName(),
                extractedGenericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromGenericField() {
        Field genericField = Stream.of(TestData.class.getDeclaredFields())
            .filter(field -> "genericField".equals(field.getName()))
            .findFirst()
            .get();
        log.debug("GenericField : {}", genericField);
        String extractedGenericType = wr.extractGenericType(genericField).iterator().next().getTypeName();
        log.debug("Extracted Type : {}", extractedGenericType);

        Assertions.assertEquals(
            Integer.class.getName(),
            extractedGenericType,
            String.format(
                "Extracted generic type expected to be '%s' but it's '%s'", String.class.getName(), extractedGenericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromWildCardField() {
        Field genericField = Stream.of(TestData.class.getDeclaredFields())
            .filter(field -> "wildCardField".equals(field.getName()))
            .findFirst()
            .get();
        log.debug("WildCardField : {}", genericField);
        String extractedGenericType = wr.extractGenericType(genericField).iterator().next().getTypeName();
        log.debug("Extracted Type : {}", extractedGenericType);

        Assertions.assertEquals(
            "?",
            extractedGenericType,
            String.format(
                "Extracted generic type expected to be '%s' but it's '%s'", String.class.getName(), extractedGenericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromMultiGenericField() {
        Field genericField = Stream.of(TestData.class.getDeclaredFields())
            .filter(field -> "multiGenericField".equals(field.getName()))
            .findFirst()
            .get();
        log.debug("MultiGenericField : {}", genericField);
        List<Type> extractedGenericType = wr.extractGenericType(genericField);
        log.debug("Extracted Type : {}", extractedGenericType);

        List<String> actual = extractedGenericType.stream().map(Type::getTypeName).collect(Collectors.toList());

        List<String> expected = List.of(BigInteger.class.getName(), String.class.getName());

        Assertions.assertEquals(
            expected,
            actual,
            String.format(
                "Extracted generic type expected to be %s, but it's %s", expected, actual
            )
        );
    }

    @Test
    void testExtractGenericTypeFromGenericClass() {
        List<Type> genericType = wr.extractGenericType(GenericClass.class);
        log.debug("Generic Type : {}", genericType);
        Assertions.assertEquals(
            List.of(TestData.class),
            genericType,
            String.format(
                "Extracted generic type expected to be %s, but it's %s", List.of(TestData.class), genericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromMultiGenericClass() {
        List<Type> genericType = wr.extractGenericType(MultiGenericClass.class);
        log.debug("Generic Type : {}", genericType);
        Assertions.assertEquals(
            List.of(BigInteger.class, TestData.class),
            genericType,
            String.format(
                "Extracted generic type expected to be %s, but it's %s",
                List.of(BigInteger.class, TestData.class),
                genericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromNonGenericClass() {
        List<Type> genericType = wr.extractGenericType(NonGenericClass.class);
        log.debug("Generic Type : {}", genericType);
        Assertions.assertEquals(
            List.of(NonGenericClass.class),
            genericType,
            String.format(
                "Extracted generic type expected to be %s, but it's %s",
                List.of(NonGenericClass.class),
                genericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromStandaloneGenericClass() {
        List<Type> genericType = wr.extractGenericType(StandaloneGenericClass.class);
        log.debug("Generic Type : {}", genericType);
        Assertions.assertEquals(
            List.of(StandaloneGenericClass.class),
            genericType,
            String.format(
                "Extracted generic type expected to be %s, but it's %s",
                List.of(StandaloneGenericClass.class),
                genericType
            )
        );
    }

    @Test
    void testExtractGenericTypeFromStandaloneNonGenericClass() {
        List<Type> genericType = wr.extractGenericType(StandaloneNonGenericClass.class);
        log.debug("Generic Type : {}", genericType);
        Assertions.assertEquals(
            List.of(StandaloneNonGenericClass.class),
            genericType,
            String.format(
                "Extracted generic type expected to be %s, but it's %s",
                List.of(StandaloneNonGenericClass.class),
                genericType
            )
        );
    }
}
