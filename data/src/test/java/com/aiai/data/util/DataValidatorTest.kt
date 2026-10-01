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
package com.aiai.data.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * DataValidator 单元测试。
 */
class DataValidatorTest {

    private val validator = DataValidator()

    @Test
    fun testValidateEmail_valid() {
        val result = validator.validateEmail("test@example.com")
        assertTrue(result)
    }

    @Test
    fun testValidateEmail_invalid() {
        val result = validator.validateEmail("invalid-email")
        assertFalse(result)
    }

    @Test
    fun testValidateEmail_empty() {
        val result = validator.validateEmail("")
        assertFalse(result)
    }

    @Test
    fun testValidatePhone_valid() {
        val result = validator.validatePhone("13800138000")
        assertTrue(result)
    }

    @Test
    fun testValidatePhone_invalid() {
        val result = validator.validatePhone("12345")
        assertFalse(result)
    }

    @Test
    fun testValidatePassword_valid() {
        val result = validator.validatePassword("password123")
        assertTrue(result)
    }

    @Test
    fun testValidatePassword_tooShort() {
        val result = validator.validatePassword("123")
        assertFalse(result)
    }

    @Test
    fun testValidateUsername_valid() {
        val result = validator.validateUsername("user123")
        assertTrue(result)
    }

    @Test
    fun testValidateUsername_tooShort() {
        val result = validator.validateUsername("ab")
        assertFalse(result)
    }

    @Test
    fun testValidateUrl_valid() {
        val result = validator.validateUrl("https://example.com")
        assertTrue(result)
    }

    @Test
    fun testValidateUrl_invalid() {
        val result = validator.validateUrl("not-a-url")
        assertFalse(result)
    }
}
