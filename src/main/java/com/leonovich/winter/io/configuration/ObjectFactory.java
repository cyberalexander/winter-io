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

package com.leonovich.winter.io.configuration;

import com.leonovich.winter.io.configurators.ObjectConfigurator;
import com.leonovich.winter.io.exceptions.WinterException;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created : 15/12/2021 18:03
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
public class ObjectFactory {
    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators;

    @SneakyThrows
    public ObjectFactory(final ApplicationContext applicationContext) {
        this.context = applicationContext;
        this.configurators = new ArrayList<>();

        /*
        Instantiate inner Reflections object here instead of using WinterReflections is required because
        configurators search should be performed in winter-io package not in client app package, which passed to
        WinterReflections
         */
        //TODO think about if it's possible to instantiate WinterReflections with multiple packages
        Reflections internalScanner = new Reflections("com.leonovich.winter.io");

        //[1] Instantiating object configurators on ObjectFactory initialization
        Set<Class<? extends ObjectConfigurator>> configuratorImplClasses = internalScanner.getSubTypesOf(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> clazz : configuratorImplClasses) {
            this.configurators.add(clazz.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(final Class<T> implClass) {
        final T t = create(implClass);
        configure(t);
        postConstruct(implClass, t);
        return t;
    }

    private <T> T create(final Class<T> implClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(final T t) {
        configurators.forEach(configurator -> configurator.configure(t, context));
    }

    private <T> void postConstruct(final Class<T> implClass, final T t) {
        Arrays.stream(implClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(PostConstruct.class))
            .forEach(method -> {
                try {
                    method.invoke(t);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new WinterException(String.format(WinterException.ErrorMessage.POST_CONSTRUCT_FAILED, t), e);
                }
            });
    }

    @Override
    public String toString() {
        return "ObjectFactory{"
            + super.toString()
            + "; configuratorsSize=" + configurators.size()
            + '}';
    }
}
