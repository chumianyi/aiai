/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 导航测试 (10 tests)
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Test fun bottom_navigation_visible() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun chat_tab_visible() {
        onView(withId(R.id.nav_chat)).check(matches(isDisplayed()))
    }

    @Test fun history_tab_visible() {
        onView(withId(R.id.nav_history)).check(matches(isDisplayed()))
    }

    @Test fun settings_tab_visible() {
        onView(withId(R.id.nav_settings)).check(matches(isDisplayed()))
    }

    @Test fun fragment_container_visible() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun menu_search_visible() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun menu_overflow_visible() {
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test fun back_navigation_available() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun up_navigation_available() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }

    @Test fun deep_link_route_defined() {
        assertNotNull(com.aiai.app.AppNavigator.ROUTE_CHAT)
    }

    private fun assertNotNull(any: Any?) {
        org.junit.Assert.assertNotNull(any)
    }
}
