/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 集合扩展测试 (10 tests)
 */
class CollectionExtTest {

    @Test fun list_size_isCorrect() {
        assertEquals(3, listOf(1, 2, 3).size)
    }

    @Test fun list_first_isCorrect() {
        assertEquals(1, listOf(1, 2, 3).first())
    }

    @Test fun list_last_isCorrect() {
        assertEquals(3, listOf(1, 2, 3).last())
    }

    @Test fun list_filter_isCorrect() {
        val evens = listOf(1, 2, 3, 4).filter { it % 2 == 0 }
        assertEquals(2, evens.size)
    }

    @Test fun list_map_isCorrect() {
        val doubled = listOf(1, 2, 3).map { it * 2 }
        assertEquals(6, doubled[2])
    }

    @Test fun list_contains_isCorrect() {
        assertTrue(listOf("a", "b").contains("a"))
    }

    @Test fun list_empty_isEmpty() {
        assertTrue(emptyList<Int>().isEmpty())
    }

    @Test fun map_entries_isCorrect() {
        val map = mapOf("a" to 1, "b" to 2)
        assertEquals(2, map.size)
        assertEquals(1, map["a"])
    }

    @Test fun set_unique_elements() {
        val set = setOf(1, 1, 2, 2, 3)
        assertEquals(3, set.size)
    }

    @Test fun list_sum_isCorrect() {
        assertEquals(6, listOf(1, 2, 3).sum())
    }
}
