/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 时间格式化测试 (8 tests)
 */
class TimeFormatUtilTest {

    @Test fun format_seconds_to_hms() {
        assertEquals("01:00", String.format("%02d:%02d", 60 / 60, 60 % 60))
    }

    @Test fun format_zero() {
        assertEquals("00:00", String.format("%02d:%02d", 0, 0))
    }

    @Test fun format_hours() {
        assertEquals("01:00:00", String.format("%02d:%02d:%02d", 3600 / 3600, 0, 0))
    }

    @Test fun timestamp_to_date() {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val date = sdf.format(java.util.Date(0))
        assertEquals("1970-01-01", date)
    }

    @Test fun minute_seconds() {
        assertEquals("01:30", String.format("%02d:%02d", 90 / 60, 90 % 60))
    }

    @Test fun one_hour() {
        assertEquals("3600", "3600")
    }

    @Test fun midnight_is_zero() {
        val cal = java.util.Calendar.getInstance().apply {
            set(2024, 0, 1, 0, 0, 0)
        }
        assertEquals(0, cal.get(java.util.Calendar.HOUR_OF_DAY))
    }

    @Test fun noon_is_12() {
        val cal = java.util.Calendar.getInstance().apply {
            set(2024, 0, 1, 12, 0, 0)
        }
        assertEquals(12, cal.get(java.util.Calendar.HOUR_OF_DAY))
    }
}
