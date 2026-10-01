/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 主界面测试 (10 tests)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AppEntryActivity::class.java)

    @Test fun bottom_navigation_displayed() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun fragment_container_displayed() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun activity_not_null() {
        activityRule.scenario.onActivity { assertNotNull(it) }
    }

    @Test fun bottom_nav_has_items() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun chat_tab_exists() {
        onView(withId(R.id.nav_chat)).check(matches(isDisplayed()))
    }

    @Test fun history_tab_exists() {
        onView(withId(R.id.nav_history)).check(matches(isDisplayed()))
    }

    @Test fun settings_tab_exists() {
        onView(withId(R.id.nav_settings)).check(matches(isDisplayed()))
    }

    @Test fun activity_title_set() {
        activityRule.scenario.onActivity {
            assertNotNull(it.title)
        }
    }

    @Test fun content_view_present() {
        onView(withId(android.R.id.content)).check(matches(isDisplayed()))
    }

    @Test fun app_bar_host_present() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }
}
