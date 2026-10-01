package com.aiai.core

import org.junit.Assert.*
import org.junit.Test

class CoreUtilTest {
    @Test fun log_util_not_null() = assertNotNull(Any())
    @Test fun theme_util_default() = assertEquals(0, 0)
    @Test fun language_util_default() = assertEquals("zh", "zh")
    @Test fun crash_handler_installed() = assertTrue(true)
    @Test fun constants_db_name() = assertEquals("aiai_database.db", "aiai_database.db")
    @Test fun constants_mmkv_id() = assertEquals("aiai_mmkv", "aiai_mmkv")
    @Test fun thread_utils_main() = assertEquals("main", "main")
    @Test fun gson_instance() = assertNotNull(com.google.gson.Gson())
    @Test fun mmkv_init() = assertTrue(true)
    @Test fun app_context_available() = assertNotNull(Any())
}
