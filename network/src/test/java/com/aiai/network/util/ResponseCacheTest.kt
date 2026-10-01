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
package com.aiai.network.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * ResponseCache 单元测试。
 */
class ResponseCacheTest {

    private val cache = ResponseCache(maxSize = 10, defaultTtl = 5000L)

    @Test
    fun testPutAndGet_returnsValue() {
        cache.put("key1", "value1")
        val result: String? = cache.get("key1")
        assertEquals("value1", result)
    }

    @Test
    fun testGet_unknownKey_returnsNull() {
        val result: String? = cache.get("unknown_key")
        assertNull(result)
    }

    @Test
    fun testContains_existingKey_returnsTrue() {
        cache.put("key2", "value2")
        assertTrue(cache.contains("key2"))
    }

    @Test
    fun testContains_unknownKey_returnsFalse() {
        assertFalse(cache.contains("unknown_key"))
    }

    @Test
    fun testRemove_existingKey_removesIt() {
        cache.put("key3", "value3")
        cache.remove("key3")
        assertFalse(cache.contains("key3"))
    }

    @Test
    fun testClear_allEntriesRemoved() {
        cache.put("key_a", "value_a")
        cache.put("key_b", "value_b")
        cache.clear()
        assertEquals(0, cache.size())
    }

    @Test
    fun testSize_returnsCorrectCount() {
        cache.clear()
        cache.put("key1", "v1")
        cache.put("key2", "v2")
        cache.put("key3", "v3")
        assertEquals(3, cache.size())
    }

    @Test
    fun testMaxSize_evictsOldest() {
        val smallCache = ResponseCache(maxSize = 3, defaultTtl = 10000L)
        smallCache.put("key1", "v1")
        smallCache.put("key2", "v2")
        smallCache.put("key3", "v3")
        smallCache.put("key4", "v4") // 应该淘汰 key1
        assertEquals(3, smallCache.size())
    }

    @Test
    fun testHitRate_initialZero() {
        val freshCache = ResponseCache()
        assertEquals(0f, freshCache.hitRate(), 0.01f)
    }

    @Test
    fun testCleanExpired_removesExpired() {
        val shortCache = ResponseCache(maxSize = 10, defaultTtl = 1L)
        shortCache.put("expire_key", "value")
        Thread.sleep(10)
        shortCache.cleanExpired()
        assertFalse(shortCache.contains("expire_key"))
    }
}
