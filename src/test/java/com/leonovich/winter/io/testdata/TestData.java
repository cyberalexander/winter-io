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

package com.leonovich.winter.io.testdata;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is class dedicated to serve as a Test Data for the unit tests.
 *
 * Created : 22/02/2022 09:15
 * Project : winter-io
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
@Data
public class TestData {
    private String nonGenericField;
    private List<Integer> genericField;
    private Set<?> wildCardField;
    private Map<BigInteger, String> multiGenericField;

    public TestData() {
        this.nonGenericField = "nonGenericField";
        this.genericField = List.of(1, 2, 3);
        this.wildCardField = Set.of(nonGenericField, 1, 2.99, BigInteger.ONE);
        this.multiGenericField = Map.of(BigInteger.ONE, nonGenericField);
    }
}
