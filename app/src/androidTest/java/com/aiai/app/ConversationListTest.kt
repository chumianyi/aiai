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
 * 会话列表测试 (10 tests)
 */
@RunWith(AndroidJUnit4::class)
class ConversationListTest {

    @Test fun new_conversation_button_exists() {
        onView(withText(R.string.menu_new_chat)).check(matches(isDisplayed()))
    }

    @Test fun search_conversation_exists() {
        onView(withText(R.string.conversation_search)).check(matches(isDisplayed()))
    }

    @Test fun empty_conversations_message_shown() {
        onView(withText(R.string.empty_conversations)).check(matches(isDisplayed()))
    }

    @Test fun today_group_label_shown() {
        onView(withText(R.string.conversation_group_today)).check(matches(isDisplayed()))
    }

    @Test fun yesterday_group_label_shown() {
        onView(withText(R.string.conversation_group_yesterday)).check(matches(isDisplayed()))
    }

    @Test fun last_7_days_label_shown() {
        onView(withText(R.string.conversation_group_last_7_days)).check(matches(isDisplayed()))
    }

    @Test fun pin_action_exists() {
        onView(withText(R.string.conversation_pin)).check(matches(isDisplayed()))
    }

    @Test fun rename_action_exists() {
        onView(withText(R.string.conversation_rename)).check(matches(isDisplayed()))
    }

    @Test fun delete_action_exists() {
        onView(withText(R.string.conversation_delete)).check(matches(isDisplayed()))
    }

    @Test fun export_action_exists() {
        onView(withText(R.string.conversation_export)).check(matches(isDisplayed()))
    }
}
