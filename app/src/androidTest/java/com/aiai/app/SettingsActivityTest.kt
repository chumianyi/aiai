/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 设置页测试 (10 tests)
 */
@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

    @Test fun settings_title_displayed() {
        onView(withText(R.string.title_settings)).check(matches(isDisplayed()))
    }

    @Test fun general_settings_shown() {
        onView(withText(R.string.settings_general)).check(matches(isDisplayed()))
    }

    @Test fun notification_settings_shown() {
        onView(withText(R.string.settings_notification)).check(matches(isDisplayed()))
    }

    @Test fun about_settings_shown() {
        onView(withText(R.string.settings_about)).check(matches(isDisplayed()))
    }

    @Test fun clear_cache_option_exists() {
        onView(withText(R.string.settings_clear_cache)).check(matches(isDisplayed()))
    }

    @Test fun check_update_option_exists() {
        onView(withText(R.string.settings_check_update)).check(matches(isDisplayed()))
    }

    @Test fun feedback_option_exists() {
        onView(withText(R.string.settings_feedback)).check(matches(isDisplayed()))
    }

    @Test fun help_option_exists() {
        onView(withText(R.string.settings_help)).check(matches(isDisplayed()))
    }

    @Test fun version_shown() {
        onView(withText(R.string.settings_version)).check(matches(isDisplayed()))
    }

    @Test fun api_config_entry_shown() {
        onView(withText(R.string.settings_api_config)).check(matches(isDisplayed()))
    }
}
