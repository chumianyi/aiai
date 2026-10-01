/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 */
package com.aiai.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 基础UI测试 (5 tests)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test fun useAppContext() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.aiai.app", context.packageName)
    }

    @Test fun app_name_resolved() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = context.getString(R.string.app_name)
        assertEquals("爱Ai", appName)
    }

    @Test fun resources_exist() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(context.resources)
    }

    @Test fun package_info() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        assertEquals(1, info.versionCode)
    }

    @Test fun app_info_label() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val label = context.applicationInfo.loadLabel(context.packageManager).toString()
        assertNotNull(label)
    }
}
