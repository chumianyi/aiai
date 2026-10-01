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
 * BiometricUtil 单元测试。
 */
class BiometricUtilTest {

    @Test
    fun testGetErrorText_unknownError() {
        val errorText = BiometricUtil.getErrorText(-999)
        assertEquals("未知错误", errorText)
    }

    @Test
    fun testGetErrorText_hwUnavailable() {
        val errorText = BiometricUtil.getErrorText(1)
        assertEquals("硬件不可用", errorText)
    }

    @Test
    fun testGetErrorText_timeout() {
        val errorText = BiometricUtil.getErrorText(10)
        assertEquals("操作超时", errorText)
    }

    @Test
    fun testGetErrorText_noBiometrics() {
        val errorText = BiometricUtil.getErrorText(11)
        assertEquals("未录入生物信息", errorText)
    }

    @Test
    fun testGetErrorText_userCanceled() {
        val errorText = BiometricUtil.getErrorText(13)
        assertEquals("用户取消", errorText)
    }

    @Test
    fun testGetErrorText_lockout() {
        val errorText = BiometricUtil.getErrorText(7)
        assertTrue(errorText.contains("锁定"))
    }

    @Test
    fun testGetErrorText_hwNotPresent() {
        val errorText = BiometricUtil.getErrorText(20)
        assertTrue(errorText.contains("不支持"))
    }

    @Test
    fun testErrorText_notEmpty() {
        for (code in 0..20) {
            val errorText = BiometricUtil.getErrorText(code)
            assertTrue("Error text for code $code should not be empty", errorText.isNotEmpty())
        }
    }

    @Test
    fun testErrorText_consistency() {
        val text1 = BiometricUtil.getErrorText(1)
        val text2 = BiometricUtil.getErrorText(1)
        assertEquals(text1, text2)
    }
}
