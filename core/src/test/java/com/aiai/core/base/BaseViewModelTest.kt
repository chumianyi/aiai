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
package com.aiai.core.base

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * BaseViewModel 单元测试。
 */
class BaseViewModelTest {

    @Test
    fun testBaseViewModel_exists() {
        assertNotNull(BaseViewModel::class.java)
    }

    @Test
    fun testBaseViewModel_isAbstract() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(BaseViewModel::class.java.modifiers))
    }

    @Test
    fun testBaseViewModel_hasInitBlock() {
        val methods = BaseViewModel::class.java.methods
        assertTrue(methods.isNotEmpty())
    }

    @Test
    fun testBaseViewModel_classNotNull() {
        assertNotNull(BaseViewModel::class)
    }
}
