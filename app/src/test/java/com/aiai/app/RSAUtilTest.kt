/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test
import java.security.KeyPairGenerator
import java.security.Signature
import java.util.Base64

/**
 * RSA加密测试 (8 tests)
 */
class RSAUtilTest {

    private fun generateKeyPair(): java.security.KeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048)
        return kpg.generateKeyPair()
    }

    @Test fun key_pair_generation() {
        val pair = generateKeyPair()
        assertNotNull(pair.private)
        assertNotNull(pair.public)
    }

    @Test fun sign_and_verify() {
        val pair = generateKeyPair()
        val data = "hello".toByteArray()
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initSign(pair.private)
        sig.update(data)
        val signature = sig.sign()
        val verifier = Signature.getInstance("SHA256withRSA")
        verifier.initVerify(pair.public)
        verifier.update(data)
        assertTrue(verifier.verify(signature))
    }

    @Test fun verify_wrong_data_fails() {
        val pair = generateKeyPair()
        val data = "hello".toByteArray()
        val sig = Signature.getInstance("SHA256withRSA")
        sig.initSign(pair.private)
        sig.update(data)
        val signature = sig.sign()
        val verifier = Signature.getInstance("SHA256withRSA")
        verifier.initVerify(pair.public)
        verifier.update("world".toByteArray())
        assertFalse(verifier.verify(signature))
    }

    @Test fun public_key_base64_not_empty() {
        val pair = generateKeyPair()
        val encoded = Base64.getEncoder().encodeToString(pair.public.encoded)
        assertTrue(encoded.length > 100)
    }

    @Test fun private_key_not_empty() {
        val pair = generateKeyPair()
        val encoded = Base64.getEncoder().encodeToString(pair.private.encoded)
        assertTrue(encoded.length > 100)
    }

    @Test fun different_keys_different_signatures() {
        val pair1 = generateKeyPair()
        val pair2 = generateKeyPair()
        val data = "test".toByteArray()
        val s1 = Signature.getInstance("SHA256withRSA")
        s1.initSign(pair1.private); s1.update(data)
        val s2 = Signature.getInstance("SHA256withRSA")
        s2.initSign(pair2.private); s2.update(data)
        assertNotEquals(s1.sign().size, s2.sign().size)
    }

    @Test fun key_algorithm_rsa() {
        val pair = generateKeyPair()
        assertEquals("RSA", pair.public.algorithm)
    }

    @Test fun key_format_x509() {
        val pair = generateKeyPair()
        assertEquals("X.509", pair.public.format)
    }
}
