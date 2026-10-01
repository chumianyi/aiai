/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test
import java.security.MessageDigest

/**
 * SHA测试 (5 tests)
 */
class SHAUtilTest {

    private fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    @Test fun sha256_of_abc() {
        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            sha256("abc")
        )
    }

    @Test fun sha256_length_64() {
        assertEquals(64, sha256("hello").length)
    }

    @Test fun sha256_is_deterministic() {
        assertEquals(sha256("test"), sha256("test"))
    }

    @Test fun sha256_different_inputs() {
        assertNotEquals(sha256("a"), sha256("b"))
    }

    @Test fun sha256_empty() {
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            sha256("")
        )
    }
}
