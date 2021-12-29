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

import com.leonovich.winter.io.exceptions.WinterException;
import com.leonovich.winter.io.model.ClassInfo;
import com.leonovich.winter.io.tools.WinterReflections;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created : 15/12/2021 18:04
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class JavaConfig implements Config {
    private final WinterReflections scanner;
    private final Map<ClassInfo, Class> ifc2implClass;

    public JavaConfig(final String packageToScan, final Map<ClassInfo, Class> interface2implClass) {
        this.scanner = new WinterReflections(packageToScan);
        this.ifc2implClass = interface2implClass;
    }

    @Override
    public <T> Class<? extends T> getImplClass(final Class<T> ifc, String genericType) {
        ClassInfo<T> key = ClassInfo.of(ifc, genericType);
        return ifc2implClass.computeIfAbsent(key, implClass -> {
            Set<Class<? extends T>> children = scanner.getSubTypesOf(ifc);
            if (CollectionUtils.isEmpty(children)) {
                throw new WinterException(String.format(WinterException.ErrorMessage.IMPLEMENTATION_NOT_FOUND, ifc.getName()));
            }
            if (children.size() > 1) {
                return children.stream()
                    .filter(cl -> genericType.equals(scanner.extractGenericType(cl)))
                    .findFirst()
                    .orElseThrow(
                        () -> new WinterException(
                            String.format(WinterException.ErrorMessage.CANNOT_FIND_IMPLEMENTATION, ifc.getName(), children)
                        )
                    );
            } else {
                return children.iterator().next();
            }
        });
    }

    @Override
    public WinterReflections scanner() {
        return this.scanner;
    }
}
