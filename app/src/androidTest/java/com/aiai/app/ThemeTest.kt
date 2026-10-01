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
 * 主题切换测试 (5 tests)
 */
@RunWith(AndroidJUnit4::class)
class ThemeTest {

    @Test fun theme_setting_displayed() {
        onView(withText(R.string.settings_theme)).check(matches(isDisplayed()))
    }

    @Test fun light_mode_option_shown() {
        onView(withText(R.string.settings_theme_light)).check(matches(isDisplayed()))
    }

    @Test fun dark_mode_option_shown() {
        onView(withText(R.string.settings_theme_dark)).check(matches(isDisplayed()))
    }

    @Test fun system_mode_option_shown() {
        onView(withText(R.string.settings_theme_system)).check(matches(isDisplayed()))
    }

    @Test fun dynamic_color_option_shown() {
        onView(withText(R.string.settings_dynamic_color)).check(matches(isDisplayed()))
    }
}
