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
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * ClipboardUtil 单元测试。
 */
class ClipboardUtilTest {

    @Test
    fun testClipboardOperations_notNull() {
        // ClipboardUtil 是单例对象，验证其存在
        assertNotNull(ClipboardUtil)
    }

    @Test
    fun testCopyText_methodExists() {
        // 验证方法存在性（实际测试需要 Android Context）
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "copyText" })
    }

    @Test
    fun testGetText_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "getText" })
    }

    @Test
    fun testHasText_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "hasText" })
    }

    @Test
    fun testClear_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "clear" })
    }

    @Test
    fun testShareText_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "shareText" })
    }

    @Test
    fun testCopyUri_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "copyUri" })
    }

    @Test
    fun testGetUri_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "getUri" })
    }

    @Test
    fun testAddClipboardListener_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "addClipboardListener" })
    }

    @Test
    fun testRemoveClipboardListener_methodExists() {
        assertNotNull(ClipboardUtil::class.java.methods.find { it.name == "removeClipboardListener" })
    }

    private fun assertNotNull(any: Any?) {
        org.junit.Assert.assertNotNull(any)
    }
}
