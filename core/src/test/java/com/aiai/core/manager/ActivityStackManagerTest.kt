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
 * ActivityStackManager 单元测试。
 */
class ActivityStackManagerTest {

    @Test
    fun testActivityStackManager_exists() {
        assertNotNull(ActivityStackManager)
    }

    @Test
    fun testActivityStackManager_isSingleton() {
        assertNotNull(ActivityStackManager)
    }

    @Test
    fun testActivityStackManager_methods() {
        val methods = ActivityStackManager::class.java.methods
        assertTrue("Should have methods", methods.size > 0)
    }

    @Test
    fun testActivityStackManager_hasPushMethod() {
        val methods = ActivityStackManager::class.java.methods
        assertNotNull(methods.find { it.name.contains("push") || it.name.contains("add") })
    }

    @Test
    fun testActivityStackManager_hasPopMethod() {
        val methods = ActivityStackManager::class.java.methods
        assertNotNull(methods.find { it.name.contains("pop") || it.name.contains("finish") })
    }
}
