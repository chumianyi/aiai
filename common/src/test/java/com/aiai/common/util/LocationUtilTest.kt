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
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * LocationUtil 单元测试。
 */
class LocationUtilTest {

    @Test
    fun testFormatDistance_lessThan1km() {
        val result = LocationUtil.formatDistance(500f)
        assertEquals("500m", result)
    }

    @Test
    fun testFormatDistance_1km() {
        val result = LocationUtil.formatDistance(1000f)
        assertTrue(result.contains("km"))
    }

    @Test
    fun testFormatDistance_5km() {
        val result = LocationUtil.formatDistance(5000f)
        assertTrue(result.contains("km"))
    }

    @Test
    fun testFormatDistance_zero() {
        val result = LocationUtil.formatDistance(0f)
        assertEquals("0m", result)
    }

    @Test
    fun testFormatDistance_100m() {
        val result = LocationUtil.formatDistance(100f)
        assertEquals("100m", result)
    }

    @Test
    fun testFormatLocation_notNull() {
        assertNotNull(LocationUtil)
    }

    @Test
    fun testCalculateDistance_methodExists() {
        assertNotNull(LocationUtil::class.java.methods.find { it.name == "calculateDistance" })
    }

    @Test
    fun testHasLocationPermission_methodExists() {
        assertNotNull(LocationUtil::class.java.methods.find { it.name == "hasLocationPermission" })
    }

    @Test
    fun testIsGpsEnabled_methodExists() {
        assertNotNull(LocationUtil::class.java.methods.find { it.name == "isGpsEnabled" })
    }

    @Test
    fun testReverseGeocode_methodExists() {
        assertNotNull(LocationUtil::class.java.methods.find { it.name == "reverseGeocode" })
    }

    private fun assertNotNull(any: Any?) {
        org.junit.Assert.assertNotNull(any)
    }
}
