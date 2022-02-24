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

package com.leonovich.winter.io.tools;

import com.leonovich.winter.io.exceptions.WinterException;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Util class, which is aimed to define functionality to resolve the type of generic of given class or field.
 * <p>
 * Created : 15/12/2021 18:06
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class WinterReflections extends Reflections {

    public WinterReflections(final String packageToScan, final Scanner... scanners) {
        super(packageToScan, scanners);
    }

    /**
     * Method extracts generic type from given field. If the field is not generic, a {@link WinterException} will
     * be thrown.
     * In case if field has generic type, then cast it to ParameterizedType and extract generic type
     * or else just return field class name.
     *
     * @param field Input field, for which need to define its generic type.
     * @return Generic type name - the full class name including package.
     */
    public List<Type> extractGenericType(final Field field) {
        List<Type> genericTypeArgs;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            genericTypeArgs = Arrays.stream(parameterizedType.getActualTypeArguments()).toList();
        } else {
            genericTypeArgs = Collections.singletonList(genericType);
        }
        return genericTypeArgs;
    }

    /**
     * Method extracts generic type from super-interface or super-class. if given class does not have
     * generic super-interface or super-class, then {@link WinterException} will be thrown
     *
     * @param implClass Input class, for which need to define its generic type based on its super-interface/class.
     * @param <T>       The input class generic type.
     * @return Generic type name - the full class name including package.
     */
    public <T> String extractGenericType(final Class<T> implClass) {
        Optional<Type> interfaceType = Arrays.stream(implClass.getGenericInterfaces()).findFirst();
        if (interfaceType.isPresent() && interfaceType.get() instanceof ParameterizedType genericInterface) {
            return Arrays.stream(genericInterface.getActualTypeArguments())
                .iterator()
                .next()
                .getTypeName();
        } else {
            Type classType = implClass.getGenericSuperclass();
            if (classType instanceof ParameterizedType superClassType) {
                return Arrays.stream(superClassType.getActualTypeArguments())
                    .iterator()
                    .next()
                    .getTypeName();
            }
        }
        throw new WinterException(
            String.format(WinterException.ErrorMessage.CANNOT_RESOLVE_GENERIC_TYPE, implClass.getName())
        );
    }
}
