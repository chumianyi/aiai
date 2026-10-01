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
 * 搜索测试 (8 tests)
 */
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @Test fun search_hint_displayed() {
        onView(withText(R.string.hint_input_search)).check(matches(isDisplayed()))
    }

    @Test fun search_history_empty_shown() {
        onView(withText(R.string.search_history_empty)).check(matches(isDisplayed()))
    }

    @Test fun result_count_format_exists() {
        onView(withText(R.string.search_result_count)).check(matches(isDisplayed()))
    }

    @Test fun search_bar_displayed() {
        onView(withText(R.string.title_search)).check(matches(isDisplayed()))
    }

    @Test fun recent_searches_section_shown() {
        onView(withText(R.string.title_history)).check(matches(isDisplayed()))
    }

    @Test fun clear_history_option_shown() {
        onView(withText(R.string.menu_clear_history)).check(matches(isDisplayed()))
    }

    @Test fun search_results_list_shown() {
        onView(withText(R.string.search)).check(matches(isDisplayed()))
    }

    @Test fun no_results_message_shown() {
        onView(withText(R.string.empty_search)).check(matches(isDisplayed()))
    }
}
