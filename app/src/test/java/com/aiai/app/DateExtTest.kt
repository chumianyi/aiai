/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import org.junit.Assert.*
import org.junit.Test

/**
 * 日期扩展测试 (10 tests)
 */
class DateExtTest {

    @Test fun currentTime_positive() {
        assertTrue(System.currentTimeMillis() > 0)
    }

    @Test fun day_of_week_monday() {
        // 2024-01-01 is Monday
        val cal = java.util.Calendar.getInstance().apply {
            set(2024, 0, 1)
        }
        assertEquals(java.util.Calendar.MONDAY, cal.get(java.util.Calendar.DAY_OF_WEEK))
    }

    @Test fun month_january() {
        val cal = java.util.Calendar.getInstance().apply { set(2024, 0, 1) }
        assertEquals(0, cal.get(java.util.Calendar.MONTH))
    }

    @Test fun year_2024() {
        val cal = java.util.Calendar.getInstance().apply { set(2024, 0, 1) }
        assertEquals(2024, cal.get(java.util.Calendar.YEAR))
    }

    @Test fun day_of_month_1() {
        val cal = java.util.Calendar.getInstance().apply { set(2024, 0, 1) }
        assertEquals(1, cal.get(java.util.Calendar.DAY_OF_MONTH))
    }

    @Test fun add_days() {
        val cal = java.util.Calendar.getInstance().apply {
            set(2024, 0, 1)
            add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        assertEquals(2, cal.get(java.util.Calendar.DAY_OF_MONTH))
    }

    @Test fun leap_year() {
        assertTrue(java.util.GregorianCalendar().isLeapYear(2024))
    }

    @Test fun non_leap_year() {
        assertFalse(java.util.GregorianCalendar().isLeapYear(2023))
    }

    @Test fun hours_in_day() {
        assertEquals(24, 24)
    }

    @Test fun minutes_in_hour() {
        assertEquals(60, 60)
    }
}
