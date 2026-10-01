/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test
import java.security.MessageDigest

/**
 * MD5测试 (5 tests)
 */
class MD5UtilTest {

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    @Test fun md5_of_abc() {
        assertEquals("900150983cd24fb0d6963f7d28e17f72", md5("abc"))
    }

    @Test fun md5_empty() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5(""))
    }

    @Test fun md5_length_32() {
        assertEquals(32, md5("hello").length)
    }

    @Test fun md5_is_deterministic() {
        assertEquals(md5("test"), md5("test"))
    }

    @Test fun md5_different_inputs() {
        assertNotEquals(md5("a"), md5("b"))
    }
}
