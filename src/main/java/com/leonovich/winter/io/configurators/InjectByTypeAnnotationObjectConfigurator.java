/*
 * MIT License
 *
 * Copyright (c) 2021 Aliaksandr Leanovich
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

package com.leonovich.winter.io.configurators;

import com.leonovich.winter.io.annotation.InjectByType;
import com.leonovich.winter.io.configuration.ApplicationContext;
import com.leonovich.winter.io.exceptions.WinterException;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Stream;

/**
 * Created : 19/12/2021 10:36
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object t, ApplicationContext context) {
        log.debug("Configuring {}", t);
        Stream.of(t.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(InjectByType.class))
            .forEach(field -> {
                field.setAccessible(true);
                Object injectCandidate = context.getObject(
                    field.getType(),
                    context.getConfig().scanner().extractGenericType(field)
                );
                /*
                t - object for which field should be populated
                injectCandidate - value which should be populated in field
                */
                try {
                    field.set(t, injectCandidate);
                } catch (IllegalAccessException e) {
                    throw new WinterException(
                        String.format(WinterException.ErrorMessage.CANNOT_INJECT_BEAN, field.getName(), injectCandidate, t),
                        e
                    );
                }
            });
    }
}
