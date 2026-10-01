/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

/**
 * AES加密测试 (10 tests)
 */
class AESUtilTest {

    private val key = "0123456789abcdef".toByteArray()

    private fun encrypt(data: String, key: ByteArray): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
    }

    private fun decrypt(data: String, key: ByteArray): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"))
        return String(cipher.doFinal(Base64.getDecoder().decode(data)))
    }

    @Test fun encrypt_produces_base64() {
        val encrypted = encrypt("hello", key)
        assertNotNull(encrypted)
        assertTrue(encrypted.length > 0)
    }

    @Test fun decrypt_recovers_original() {
        val original = "hello world"
        val encrypted = encrypt(original, key)
        val decrypted = decrypt(encrypted, key)
        assertEquals(original, decrypted)
    }

    @Test fun empty_string_roundtrip() {
        val original = ""
        val encrypted = encrypt(original, key)
        val decrypted = decrypt(encrypted, key)
        assertEquals(original, decrypted)
    }

    @Test fun long_string_roundtrip() {
        val original = "A".repeat(1000)
        val encrypted = encrypt(original, key)
        val decrypted = decrypt(encrypted, key)
        assertEquals(original, decrypted)
    }

    @Test fun special_chars_roundtrip() {
        val original = "!@#$%^&*()中文"
        val encrypted = encrypt(original, key)
        val decrypted = decrypt(encrypted, key)
        assertEquals(original, decrypted)
    }

    @Test fun different_keys_produce_different_results() {
        val key2 = "fedcba9876543210".toByteArray()
        val e1 = encrypt("test", key)
        val e2 = encrypt("test", key2)
        assertNotEquals(e1, e2)
    }

    @Test fun encrypted_not_equal_to_original() {
        val original = "secret"
        val encrypted = encrypt(original, key)
        assertNotEquals(original, encrypted)
    }

    @Test fun numeric_string_roundtrip() {
        val original = "1234567890"
        val encrypted = encrypt(original, key)
        assertEquals(original, decrypt(encrypted, key))
    }

    @Test fun unicode_roundtrip() {
        val original = "你好，世界"
        val encrypted = encrypt(original, key)
        assertEquals(original, decrypt(encrypted, key))
    }

    @Test fun whitespace_roundtrip() {
        val original = "  hello world  "
        val encrypted = encrypt(original, key)
        assertEquals(original, decrypt(encrypted, key))
    }
}
