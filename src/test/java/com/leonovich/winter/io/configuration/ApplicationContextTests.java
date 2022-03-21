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

package com.leonovich.winter.io.configuration;

import com.leonovich.winter.io.WinterIoAbstractTests;
import com.leonovich.winter.io.testdata.GenericClass;
import com.leonovich.winter.io.testdata.GenericInterface;
import com.leonovich.winter.io.testdata.StandaloneGenericClass;
import com.leonovich.winter.io.testdata.MultiGenericClass;
import com.leonovich.winter.io.testdata.MultiGenericInterface;
import com.leonovich.winter.io.testdata.NonGenericClass;
import com.leonovich.winter.io.testdata.NonGenericInterface;
import com.leonovich.winter.io.testdata.TestData;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created : 28/02/2022 09:59
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
class ApplicationContextTests extends WinterIoAbstractTests {

    private static Stream<Arguments> getObjectReturnsNotNullSource() {
        return Stream.of(
            Arguments.of(GenericClass.class),
            Arguments.of(NonGenericClass.class),
            Arguments.of(StandaloneGenericClass.class),
            Arguments.of(MultiGenericInterface.class)
        );
    }

    @ParameterizedTest
    @MethodSource("getObjectReturnsNotNullSource")
    void testGetObjectReturnsNotNull(final Class<?> input) {
        Object instance = context().getObject(input);
        Assertions.assertNotNull(instance,
            String.format(
                "Expected to get new instance from %s class, but was NULL.",
                input.getSimpleName()
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetObjectReturnsProperSingleGenericInstance() {
        GenericInterface<TestData> instance = context().getObject(GenericInterface.class, List.of(TestData.class));

        if (log.isDebugEnabled()) {
            log.debug("Retrieved instance of {}", instance.getClass().getSimpleName());
        }

        Assertions.assertInstanceOf(GenericClass.class,
            instance,
            String.format(
                "Expected to get new instance of %s class, but retrieved %s.",
                GenericClass.class.getSimpleName(), instance.getClass().getSimpleName()
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetObjectReturnsProperMultipleGenericInstance() {
        MultiGenericInterface<BigInteger, TestData> instance = context().getObject(
            MultiGenericInterface.class,
            List.of(BigInteger.class, TestData.class)
        );

        if (log.isDebugEnabled()) {
            log.debug("Retrieved instance of {}", instance.getClass().getSimpleName());
        }

        Assertions.assertInstanceOf(MultiGenericClass.class,
            instance,
            String.format(
                "Expected to get new instance of %s class, but retrieved %s.",
                MultiGenericClass.class.getSimpleName(), instance.getClass().getSimpleName()
            )
        );
    }

    @Test
    void testGetObjectReturnsProperNonGenericInstance() {
        NonGenericInterface instance = context().getObject(NonGenericInterface.class);

        if (log.isDebugEnabled()) {
            log.debug("Retrieved instance of {}", instance.getClass().getSimpleName());
        }

        Assertions.assertInstanceOf(NonGenericClass.class,
            instance,
            String.format(
                "Expected to get new instance of %s class, but retrieved %s.",
                NonGenericClass.class.getSimpleName(), instance.getClass().getSimpleName()
            )
        );
    }
}
