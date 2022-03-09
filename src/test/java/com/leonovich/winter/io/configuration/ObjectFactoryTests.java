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

import com.leonovich.winter.io.testdata.AbstractClass;
import com.leonovich.winter.io.testdata.GenericInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Created : 28/02/2022 09:59
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
class ObjectFactoryTests {
    private static ObjectFactory factory;

    @BeforeAll
    static void beforeAll() {
        Config config = new JavaConfig("com.leonovich.winter.io.testdata", new HashMap<>());
        ApplicationContext context = new ApplicationContext(config);
        factory = new ObjectFactory(context);
        context.setFactory(factory);
    }

    /**
     * Exception expected to be thrown, in case if {@link ObjectFactory#createObject(Class)} called
     * for the interface class.
     */
    @Test
    void testCreateObjectThrowsExceptionWhenInterfacePassed() {
        Assertions.assertThrows(
            NoSuchMethodException.class,
            () -> factory.createObject(GenericInterface.class)
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
            () -> factory.createObject(AbstractClass.class)
        );
    }
}
