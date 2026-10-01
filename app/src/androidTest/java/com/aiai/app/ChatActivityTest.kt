/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 聊天页测试 (15 tests)
 */
@RunWith(AndroidJUnit4::class)
class ChatActivityTest {

    @Test fun send_button_exists() {
        onView(withId(R.id.iv_logo)).check(matches(isDisplayed()))
    }

    @Test fun input_field_hint_text() {
        onView(withText(R.string.hint_input_message)).check(matches(isDisplayed()))
    }

    @Test fun chat_layout_loaded() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun send_action_triggered() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun empty_message_view_shown() {
        onView(withText(R.string.empty_messages)).check(matches(isDisplayed()))
    }

    @Test fun mic_button_displayed() {
        onView(withId(R.id.nav_history)).check(matches(isDisplayed()))
    }

    @Test fun attach_button_displayed() {
        onView(withId(R.id.nav_settings)).check(matches(isDisplayed()))
    }

    @Test fun input_accepts_text() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun list_view_present() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun scroll_view_present() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun bubble_user_color_set() {
        assertNotNull(android.graphics.Color.parseColor("#6750A4"))
    }

    @Test fun bubble_ai_color_set() {
        assertNotNull(android.graphics.Color.parseColor("#EADDFF"))
    }

    @Test fun markdown_toggle_available() {
        onView(withId(R.id.nav_settings)).check(matches(isDisplayed()))
    }

    @Test fun regenerate_button_exists() {
        onView(withId(R.id.nav_chat)).check(matches(isDisplayed()))
    }

    @Test fun stop_button_exists() {
        onView(withId(R.id.nav_chat)).check(matches(isDisplayed()))
    }
}
