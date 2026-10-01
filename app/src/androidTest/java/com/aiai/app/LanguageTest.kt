/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 语言切换测试 (4 tests)
 */
@RunWith(AndroidJUnit4::class)
class LanguageTest {

    @Test fun language_setting_displayed() {
        onView(withText(R.string.title_language)).check(matches(isDisplayed()))
    }

    @Test fun chinese_option_shown() {
        onView(withText(R.string.language_chinese)).check(matches(isDisplayed()))
    }

    @Test fun english_option_shown() {
        onView(withText(R.string.language_english)).check(matches(isDisplayed()))
    }

    @Test fun system_language_option_shown() {
        onView(withText(R.string.language_system)).check(matches(isDisplayed()))
    }
}
