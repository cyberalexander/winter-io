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

import com.leonovich.winter.io.testdata.GenericClass;
import com.leonovich.winter.io.testdata.GenericInterface;
import com.leonovich.winter.io.testdata.MultiGenericClass;
import com.leonovich.winter.io.testdata.MultiGenericInterface;
import com.leonovich.winter.io.testdata.TestData;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * Created : 28/02/2022 09:59
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
class ApplicationContextTests {
    private static ApplicationContext context;

    @BeforeAll
    static void beforeAll() {
        Config config = new JavaConfig("com.leonovich.winter.io.testdata", new HashMap<>());
        context = new ApplicationContext(config);
        ObjectFactory factory = new ObjectFactory(context);
        context.setFactory(factory);
    }

    @Test
    void testGetObjectReturnsNotNull() {
        GenericClass instance = context.getObject(GenericClass.class);
        Assertions.assertNotNull(instance,
            String.format(
                "Expected to get new instance from %s class, but was NULL.",
                GenericClass.class.getSimpleName()
            )
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetObjectReturnsProperInstance() {
        GenericInterface<TestData> instance = context.getObject(GenericInterface.class, List.of(TestData.class));

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
    void testGetObjectReturnsProperInstance1() {
        MultiGenericInterface<BigInteger, TestData> instance = context.getObject(
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
                GenericClass.class.getSimpleName(), instance.getClass().getSimpleName()
            )
        );
    }
}
