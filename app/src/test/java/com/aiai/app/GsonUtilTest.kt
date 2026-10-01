/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

/**
 * JSON测试 (5 tests)
 */
class GsonUtilTest {

    data class User(val name: String, val age: Int)

    private val gson = Gson()

    @Test fun to_json() {
        val json = gson.toJson(User("Alice", 30))
        assertTrue(json.contains("Alice"))
    }

    @Test fun from_json() {
        val user = gson.fromJson("""{"name":"Bob","age":25}""", User::class.java)
        assertEquals("Bob", user.name)
        assertEquals(25, user.age)
    }

    @Test fun roundtrip() {
        val user = User("Carol", 40)
        val json = gson.toJson(user)
        val restored = gson.fromJson(json, User::class.java)
        assertEquals(user, restored)
    }

    @Test fun null_fields() {
        val json = gson.toJson(User("", 0))
        assertNotNull(json)
    }

    @Test fun list_serialization() {
        val list = listOf(User("a", 1), User("b", 2))
        val json = gson.toJson(list)
        assertTrue(json.contains("a"))
    }
}
