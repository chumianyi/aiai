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
 * 提示词库测试 (8 tests)
 */
@RunWith(AndroidJUnit4::class)
class PromptLibraryTest {

    @Test fun library_title_displayed() {
        onView(withText(R.string.title_prompt_library)).check(matches(isDisplayed()))
    }

    @Test fun all_category_shown() {
        onView(withText(R.string.prompt_category_all)).check(matches(isDisplayed()))
    }

    @Test fun writing_category_shown() {
        onView(withText(R.string.prompt_category_writing)).check(matches(isDisplayed()))
    }

    @Test fun coding_category_shown() {
        onView(withText(R.string.prompt_category_coding)).check(matches(isDisplayed()))
    }

    @Test fun learning_category_shown() {
        onView(withText(R.string.prompt_category_learning)).check(matches(isDisplayed()))
    }

    @Test fun use_prompt_button_exists() {
        onView(withText(R.string.prompt_use)).check(matches(isDisplayed()))
    }

    @Test fun edit_prompt_button_exists() {
        onView(withText(R.string.prompt_edit)).check(matches(isDisplayed()))
    }

    @Test fun add_custom_prompt_exists() {
        onView(withText(R.string.prompt_add_custom)).check(matches(isDisplayed()))
    }
}
