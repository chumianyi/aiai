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
 * 关于页测试 (5 tests)
 */
@RunWith(AndroidJUnit4::class)
class AboutActivityTest {

    @Test fun about_title_displayed() {
        onView(withText(R.string.title_about)).check(matches(isDisplayed()))
    }

    @Test fun version_info_shown() {
        onView(withText(R.string.about_version)).check(matches(isDisplayed()))
    }

    @Test fun developer_shown() {
        onView(withText(R.string.about_author)).check(matches(isDisplayed()))
    }

    @Test fun license_link_shown() {
        onView(withText(R.string.about_license)).check(matches(isDisplayed()))
    }

    @Test fun disclaimer_shown() {
        onView(withText(R.string.about_disclaimer)).check(matches(isDisplayed()))
    }
}
