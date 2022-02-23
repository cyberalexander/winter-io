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

package com.leonovich.winter.io;

import com.leonovich.winter.io.configuration.ApplicationContext;
import com.leonovich.winter.io.configuration.Config;
import com.leonovich.winter.io.configuration.JavaConfig;
import com.leonovich.winter.io.configuration.ObjectFactory;
import com.leonovich.winter.io.model.ClassInfo;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created : 19/12/2021 10:37
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Log4j2
public final class WinterIo {

    private WinterIo() {
    }

    public static ApplicationContext run(final String packageToScan, final Map<ClassInfo, Class> ifc2ImplClass) {
        log.info("WinterIo starting...");
        Config config = new JavaConfig(packageToScan, ifc2ImplClass);
        ApplicationContext context = new ApplicationContext(config);
        ObjectFactory factory = new ObjectFactory(context);
        context.setFactory(factory);
        log.info("WinterIo started.");
        return context;
    }

    public static void main(final String[] args) {
        String packageToScan;
        if (args.length >= 1) {
            packageToScan = args[0];
        } else {
            packageToScan = WinterIo.class.getPackageName();
        }
        log.info(packageToScan);
        ApplicationContext context = WinterIo.run(packageToScan, new HashMap<>());
        log.trace("Default context: {}", context);
    }
}
