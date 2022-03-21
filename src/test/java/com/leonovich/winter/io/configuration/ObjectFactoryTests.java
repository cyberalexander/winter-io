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
import com.leonovich.winter.io.testdata.AbstractClass;
import com.leonovich.winter.io.testdata.AbstractClassImpl;
import com.leonovich.winter.io.testdata.GenericClass;
import com.leonovich.winter.io.testdata.GenericInterface;
import com.leonovich.winter.io.testdata.MultiGenericClass;
import com.leonovich.winter.io.testdata.StandaloneGenericClass;
import com.leonovich.winter.io.testdata.StandaloneNonGenericClass;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
class ObjectFactoryTests extends WinterIoAbstractTests {

    /**
     * Exception expected to be thrown, in case if {@link ObjectFactory#createObject(Class)} called
     * for the interface class.
     */
    @Test
    void testCreateObjectThrowsExceptionWhenInterfacePassed() {
        Assertions.assertThrows(
            NoSuchMethodException.class,
            () -> factory().createObject(GenericInterface.class)
        );
    }

    /**
     * Exception expected to be thrown, in case if {@link ObjectFactory#createObject(Class)} called
     * for the abstract class.
     */
    @Test
    void testCreateObjectThrowsExceptionWhenAbstractClassPassed() {
        Assertions.assertThrows(
            InstantiationException.class,
            () -> factory().createObject(AbstractClass.class)
        );
    }

    @ParameterizedTest
    @MethodSource("getClassesToCreateObjects")
    void testCreateObject(final Class<?> implClass) {
        Assertions.assertNotNull(
            factory().createObject(implClass),
            "Created object must not be null."
        );
    }

    private static Stream<Arguments> getClassesToCreateObjects() {
        return Stream.of(
            Arguments.of(GenericClass.class),
            Arguments.of(MultiGenericClass.class),
            Arguments.of(StandaloneGenericClass.class),
            Arguments.of(StandaloneNonGenericClass.class),
            Arguments.of(AbstractClassImpl.class)
        );
    }
}
