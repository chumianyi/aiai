/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 数字扩展测试 (5 tests)
 */
class NumberExtTest {

    @Test fun int_parse() {
        assertEquals(123, "123".toInt())
    }

    @Test fun double_parse() {
        assertEquals(3.14, "3.14".toDouble(), 0.001)
    }

    @Test fun float_parse() {
        assertEquals(1.5f, "1.5".toFloat(), 0.001f)
    }

    @Test fun long_parse() {
        assertEquals(1000000L, "1000000".toLong())
    }

    @Test fun number_format() {
        assertEquals("3.14", "%.2f".format(3.14159))
    }
}
