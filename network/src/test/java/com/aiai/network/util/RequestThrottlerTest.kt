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
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * RequestThrottler 单元测试。
 */
class RequestThrottlerTest {

    private val throttler = RequestThrottler(1000L)

    @Test
    fun testFirstRequest_allowed() {
        val result = throttler.shouldRequest("test_key")
        assertTrue("First request should be allowed", result)
    }

    @Test
    fun testSecondRequest_throttled() {
        throttler.shouldRequest("test_key_2")
        val result = throttler.shouldRequest("test_key_2")
        assertFalse("Second request within interval should be throttled", result)
    }

    @Test
    fun testDifferentKeys_independent() {
        throttler.shouldRequest("key_a")
        val result = throttler.shouldRequest("key_b")
        assertTrue("Different keys should be independent", result)
    }

    @Test
    fun testClear_allowsRequest() {
        throttler.shouldRequest("test_key_3")
        throttler.clear("test_key_3")
        val result = throttler.shouldRequest("test_key_3")
        assertTrue("After clear, request should be allowed", result)
    }

    @Test
    fun testClearAll_allowsAll() {
        throttler.shouldRequest("key_a")
        throttler.shouldRequest("key_b")
        throttler.clearAll()
        assertTrue(throttler.shouldRequest("key_a"))
        assertTrue(throttler.shouldRequest("key_b"))
    }

    @Test
    fun testRemainingTime_positive() {
        throttler.shouldRequest("test_key_4")
        val remaining = throttler.remainingTime("test_key_4")
        assertTrue("Remaining time should be positive", remaining > 0)
    }

    @Test
    fun testRemainingTime_unknownKey() {
        val remaining = throttler.remainingTime("unknown_key")
        assertEquals("Unknown key should have 0 remaining time", 0L, remaining)
    }

    @Test
    fun testCustomInterval() {
        val customThrottler = RequestThrottler(5000L)
        assertTrue(customThrottler.shouldRequest("custom_key"))
        assertFalse(customThrottler.shouldRequest("custom_key"))
    }
}
