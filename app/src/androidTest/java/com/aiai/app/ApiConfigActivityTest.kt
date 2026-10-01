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
 * API配置测试 (10 tests)
 */
@RunWith(AndroidJUnit4::class)
class ApiConfigActivityTest {

    @Test fun api_url_field_displayed() {
        onView(withText(R.string.settings_api_url)).check(matches(isDisplayed()))
    }

    @Test fun api_key_field_displayed() {
        onView(withText(R.string.settings_api_key)).check(matches(isDisplayed()))
    }

    @Test fun model_field_displayed() {
        onView(withText(R.string.settings_model)).check(matches(isDisplayed()))
    }

    @Test fun temperature_slider_displayed() {
        onView(withText(R.string.settings_temperature)).check(matches(isDisplayed()))
    }

    @Test fun max_tokens_field_displayed() {
        onView(withText(R.string.settings_max_tokens)).check(matches(isDisplayed()))
    }

    @Test fun stream_toggle_displayed() {
        onView(withText(R.string.settings_stream)).check(matches(isDisplayed()))
    }

    @Test fun timeout_field_displayed() {
        onView(withText(R.string.settings_timeout)).check(matches(isDisplayed()))
    }

    @Test fun system_prompt_displayed() {
        onView(withText(R.string.settings_system_prompt)).check(matches(isDisplayed()))
    }

    @Test fun test_connection_button_exists() {
        onView(withText(R.string.api_config_test_connection)).check(matches(isDisplayed()))
    }

    @Test fun reset_default_button_exists() {
        onView(withText(R.string.api_config_reset_default)).check(matches(isDisplayed()))
    }
}
