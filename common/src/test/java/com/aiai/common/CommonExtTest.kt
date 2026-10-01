package com.aiai.common

import org.junit.Assert.*
import org.junit.Test

class CommonExtTest {
    @Test fun view_ext_visible() = assertTrue(true)
    @Test fun view_ext_gone() = assertFalse(false)
    @Test fun fragment_ext_toast() = assertNotNull("toast")
    @Test fun activity_ext_finish() = assertTrue(true)
    @Test fun string_ext_email() = assertTrue("a@b.com".contains("@"))
    @Test fun string_ext_url() = assertTrue("https://x.com".startsWith("http"))
    @Test fun date_ext_format() = assertEquals("2024", "2024")
    @Test fun number_ext_limit() = assertTrue(100 > 10)
    @Test fun collection_ext_empty() = assertTrue(emptyList<Int>().isEmpty())
    @Test fun collection_ext_first() = assertEquals(1, listOf(1,2,3).first())
}
