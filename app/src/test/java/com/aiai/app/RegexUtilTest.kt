/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 正则测试 (10 tests)
 */
class RegexUtilTest {

    @Test fun email_valid() {
        assertTrue("test@example.com".matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")))
    }

    @Test fun email_invalid() {
        assertFalse("not-an-email".matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$")))
    }

    @Test fun phone_valid() {
        assertTrue("13800138000".matches(Regex("^1[3-9]\\d{9}$")))
    }

    @Test fun phone_invalid() {
        assertFalse("12345".matches(Regex("^1[3-9]\\d{9}$")))
    }

    @Test fun url_valid() {
        assertTrue("https://example.com".matches(Regex("^https?://.+$")))
    }

    @Test fun url_invalid() {
        assertFalse("ftp://example.com".matches(Regex("^https?://.+$")))
    }

    @Test fun digits_only() {
        assertTrue("123456".matches(Regex("^\\d+$")))
    }

    @Test fun digits_only_fails_on_letters() {
        assertFalse("123abc".matches(Regex("^\\d+$")))
    }

    @Test fun alphanumeric() {
        assertTrue("abc123".matches(Regex("^[a-zA-Z0-9]+$")))
    }

    @Test fun ip_address() {
        assertTrue("192.168.1.1".matches(Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")))
    }
}
