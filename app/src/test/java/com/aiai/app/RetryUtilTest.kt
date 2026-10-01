/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 重试工具测试 (5 tests)
 */
class RetryUtilTest {

    private var attemptCount = 0

    private fun <T> retry(maxAttempts: Int, block: () -> T): T {
        var lastError: Exception? = null
        repeat(maxAttempts) {
            try {
                return block()
            } catch (e: Exception) {
                lastError = e
            }
        }
        throw lastError ?: Exception("Retry failed")
    }

    @Test fun retry_succeeds_first_try() {
        attemptCount = 0
        val result = retry(3) {
            attemptCount++
            "success"
        }
        assertEquals("success", result)
        assertEquals(1, attemptCount)
    }

    @Test fun retry_succeeds_on_second_try() {
        attemptCount = 0
        val result = retry(3) {
            attemptCount++
            if (attemptCount < 2) throw RuntimeException("fail")
            "success"
        }
        assertEquals("success", result)
        assertEquals(2, attemptCount)
    }

    @Test fun retry_fails_after_max() {
        attemptCount = 0
        var failed = false
        try {
            retry(2) {
                attemptCount++
                throw RuntimeException("always fail")
            }
        } catch (e: Exception) {
            failed = true
        }
        assertTrue(failed)
        assertEquals(2, attemptCount)
    }

    @Test fun retry_zero_throws() {
        var failed = false
        try {
            retry(0) { "x" }
        } catch (e: Exception) {
            failed = true
        }
        assertTrue(failed)
    }

    @Test fun retry_3_times_completes() {
        attemptCount = 0
        val result = retry(5) {
            attemptCount++
            "done"
        }
        assertEquals("done", result)
    }
}
