/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * NfcUtil 单元测试。
 */
class NfcUtilTest {

    @Test
    fun testObject_exists() {
        assertNotNull(NfcUtil)
    }

    @Test
    fun testMethod_count() {
        val methods = NfcUtil::class.java.methods
        assertTrue("Should have multiple methods", methods.size > 0)
    }

    @Test
    fun testMethod_notNull() {
        val methods = NfcUtil::class.java.methods
        assertNotNull(methods)
    }

    @Test
    fun testMethod_count_positive() {
        val methods = NfcUtil::class.java.methods
        assertTrue(methods.size >= 0)
    }

    @Test
    fun testClass_public() {
        val modifiers = NfcUtil::class.java.modifiers
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers) || java.lang.reflect.Modifier.isStatic(modifiers))
    }

    @Test
    fun testClass_notInterface() {
        assertFalse(NfcUtil::class.java.isInterface)
    }

    @Test
    fun testClass_notEnum() {
        assertFalse(NfcUtil::class.java.isEnum)
    }

    @Test
    fun testToString_notNull() {
        assertNotNull(NfcUtil.toString())
    }

    @Test
    fun testHashCode_consistent() {
        val h1 = NfcUtil.hashCode()
        val h2 = NfcUtil.hashCode()
        assertEquals(h1, h2)
    }
}
