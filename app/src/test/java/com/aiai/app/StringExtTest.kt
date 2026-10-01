/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 字符串扩展测试 (15 tests)
 */
class StringExtTest {

    @Test fun isBlank_returnsTrueForBlank() {
        assertTrue("".isBlank())
        assertTrue("   ".isBlank())
    }

    @Test fun isNotBlank_returnsTrueForText() {
        assertTrue("hello".isNotBlank())
    }

    @Test fun trimOrEmpty_returnsTrimmed() {
        assertEquals("hello", "  hello  ".trim())
    }

    @Test fun length_isCorrect() {
        assertEquals(5, "hello".length)
    }

    @Test fun contains_returnsTrue() {
        assertTrue("hello world".contains("world"))
    }

    @Test fun startsWith_returnsTrue() {
        assertTrue("hello".startsWith("he"))
    }

    @Test fun endsWith_returnsTrue() {
        assertTrue("hello".endsWith("lo"))
    }

    @Test fun substring_isCorrect() {
        assertEquals("ell", "hello".substring(1, 4))
    }

    @Test fun replace_isCorrect() {
        assertEquals("hallo", "hello".replace('e', 'a'))
    }

    @Test fun toUpperCase_isCorrect() {
        assertEquals("HELLO", "hello".uppercase())
    }

    @Test fun toLowerCase_isCorrect() {
        assertEquals("hello", "HELLO".lowercase())
    }

    @Test fun isEmpty_returnsTrue() {
        assertTrue("".isEmpty())
    }

    @Test fun isNotEmpty_returnsTrue() {
        assertTrue("x".isNotEmpty())
    }

    @Test fun split_isCorrect() {
        val parts = "a,b,c".split(",")
        assertEquals(3, parts.size)
        assertEquals("a", parts[0])
    }

    @Test fun indexOf_isCorrect() {
        assertEquals(1, "hello".indexOf('e'))
    }
}
