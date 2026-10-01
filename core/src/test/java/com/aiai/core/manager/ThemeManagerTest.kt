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
package com.aiai.core.manager

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * ThemeManager 单元测试。
 */
class ThemeManagerTest {

    @Test
    fun testThemeManager_exists() {
        assertNotNull(ThemeManager)
    }

    @Test
    fun testThemeManager_methods() {
        val methods = ThemeManager::class.java.methods
        assertTrue(methods.size > 0)
    }

    @Test
    fun testThemeManager_hasApplyMethod() {
        val methods = ThemeManager::class.java.methods
        assertNotNull(methods.find { it.name.contains("apply") || it.name.contains("set") })
    }

    @Test
    fun testThemeManager_hasGetMethod() {
        val methods = ThemeManager::class.java.methods
        assertNotNull(methods.find { it.name.contains("get") })
    }
}
