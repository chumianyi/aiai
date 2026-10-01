/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 基础单元测试
 */
class ExampleUnitTest {

    @Test fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test fun subtraction_isCorrect() {
        assertEquals(2, 5 - 3)
    }

    @Test fun multiplication_isCorrect() {
        assertEquals(12, 3 * 4)
    }

    @Test fun division_isCorrect() {
        assertEquals(3, 10 / 3)
    }

    @Test fun string_concat() {
        assertEquals("HelloWorld", "Hello" + "World")
    }
}
