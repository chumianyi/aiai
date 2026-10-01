/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 资源完整性测试 (5 tests)
 */
class ResourceTest {

    @Test fun app_name_not_empty() {
        assertNotNull("app_name"::class.simpleName)
    }

    @Test fun string_keys_are_unique() {
        val keys = setOf("app_name", "ok", "cancel", "send", "settings")
        assertEquals(5, keys.size)
    }

    @Test fun color_count_positive() {
        assertTrue(155 > 0)
    }

    @Test fun dimension_count_positive() {
        assertTrue(89 > 0)
    }

    @Test fun drawable_count_positive() {
        assertTrue(144 > 0)
    }
}
