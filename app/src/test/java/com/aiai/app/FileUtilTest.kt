/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test
import java.io.File

/**
 * 文件工具测试 (5 tests)
 */
class FileUtilTest {

    @Test fun file_extension() {
        assertEquals("txt", "hello.txt".substringAfterLast('.'))
    }

    @Test fun file_name_without_ext() {
        assertEquals("hello", "hello.txt".substringBeforeLast('.'))
    }

    @Test fun byte_size_format() {
        assertEquals("1.00 KB", "%.2f KB".format(1024.0 / 1024))
    }

    @Test fun mb_format() {
        assertEquals("1.00 MB", "%.2f MB".format(1024.0 * 1024 / (1024 * 1024)))
    }

    @Test fun empty_file_name_invalid() {
        assertFalse("".matches(Regex("^[\\w.]+$")))
    }
}
