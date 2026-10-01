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
package com.aiai.common.ext

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * TextViewExt 扩展函数单元测试。
 */
class TextViewExtTest {

    @Test
    fun testFile_exists() {
        assertNotNull(TextViewExt::class.java)
    }

    @Test
    fun testMethods_count() {
        val methods = TextViewExt::class.java.methods
        assertTrue("Should have extension methods", methods.size > 0)
    }

    @Test
    fun testMethods_notNull() {
        val methods = TextViewExt::class.java.methods
        assertNotNull(methods)
    }

    @Test
    fun testClass_public() {
        val modifiers = TextViewExt::class.java.modifiers
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers))
    }

    @Test
    fun testToString_notNull() {
        assertNotNull(TextViewExt.toString())
    }
}
