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

import com.leonovich.winter.io.annotation.Singleton;
import com.leonovich.winter.io.exceptions.WinterException;
import com.leonovich.winter.io.model.ClassInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created : 13/12/2021 09:21
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public final class ApplicationContext {

    @Setter
    private ObjectFactory factory;
    @Getter
    private final Config config;
    private final Map<ClassInfo, Object> cache = new ConcurrentHashMap<>();

    public ApplicationContext(Config config) {
        this.config = config;
    }

    public <T> T getObject(final Class<T> objectType) {
        return this.getObject(objectType, "");
    }

    public <T> T getObject(final Class<T> objectType, final String genericType) {
        //1. Creating KEY
        ClassInfo<T> key = ClassInfo.of(objectType, genericType);

        //2. Checking if Object already created and exists in cache
        if (cache.containsKey(key)) {
            return (T) cache.get(key);
        }

        //3. Searching for appropriate implementation class
        Class<? extends T> implClass = objectType;
        if (objectType.isInterface()) {
            implClass = config.getImplClass(objectType, genericType);
        }

        //4. Creating new object instance
        T t = factory.createObject(implClass);

        //5. If annotated with @Singleton annotation, caching
        if (implClass.isAnnotationPresent(Singleton.class)) {
            cache.put(ClassInfo.of(objectType, genericType), t);
        }

        return Optional.ofNullable(t)
            .orElseThrow(
                () -> new WinterException(
                    String.format(WinterException.ErrorMessage.CANNOT_GET_OBJECT_INSTANCE, objectType, genericType)
                )
            );
    }
}
