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

import com.leonovich.winter.io.exceptions.WinterException;
import com.leonovich.winter.io.testdata.GenericClass;
import com.leonovich.winter.io.testdata.GenericInterface;
import com.leonovich.winter.io.testdata.NonGenericClass;
import com.leonovich.winter.io.testdata.NonGenericInterface;
import com.leonovich.winter.io.testdata.StandaloneGenericClass;
import com.leonovich.winter.io.testdata.TestData;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created : 27/02/2022 10:38
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
class JavaConfigTests {
    private final JavaConfig jc = new JavaConfig("com.leonovich.winter.io.testdata", new HashMap<>());

    @Test
    void testGetImplClassFromGenericInterface() {
        Class<? extends GenericInterface> implClass = jc.getImplClass(GenericInterface.class, List.of(TestData.class));
        log.debug("Implementation: {}", implClass);
        Assertions.assertEquals(
            GenericClass.class,
            implClass,
            String.format(
                "Implementation of %s class expected to be '%s' but it's '%s'",
                GenericInterface.class.getSimpleName(),
                GenericClass.class.getSimpleName(),
                implClass.getSimpleName()
            )
        );
    }

    @Test
    void testGetImplClassFromNonGenericInterface() {
        Class<? extends NonGenericInterface> ic = jc.getImplClass(NonGenericInterface.class, Collections.emptyList());
        log.debug("Implementation: {}", ic);
        Assertions.assertEquals(
            NonGenericClass.class,
            ic,
            String.format(
                "Implementation of %s class expected to be '%s' but it's '%s'",
                NonGenericInterface.class.getSimpleName(),
                NonGenericClass.class.getSimpleName(),
                ic.getSimpleName()
            )
        );
    }

    /**
     * Exception expected to be thrown, in case if {@link JavaConfig#getImplClass} called
     * for the class or interface which does not have implementations/children.
     */
    @Test
    void testGetImplClassThrowsException() {
        Assertions.assertThrows(
            WinterException.class,
            () -> jc.getImplClass(StandaloneGenericClass.class, Collections.emptyList())
        );
    }

    @Test
    void testGetImplClassThrowsException1() {
        WinterException exception = Assertions.assertThrows(
            WinterException.class,
            () -> jc.getImplClass(StandaloneGenericClass.class, Collections.emptyList())
        );
        Assertions.assertEquals(
            String.format(
                WinterException.ErrorMessage.IMPLEMENTATION_NOT_FOUND, StandaloneGenericClass.class.getName()
            ),
            exception.getMessage()
        );
    }
}
