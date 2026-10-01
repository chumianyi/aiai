/*
 * Copyright (c) 2024 AiAi. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.app

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import com.aiai.core.util.LogUtil
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 应用生命周期观察者
 *
 * - 监听前后台切换
 * - 统计Activity栈
 * - 监听配置变化（语言/主题）
 */
@Singleton
class AppLifecycleObserver @Inject constructor() : Application.ActivityLifecycleCallbacks, ComponentCallbacks {

    companion object {
        private const val TAG = "AppLifecycleObserver"
    }

    private var activityCount = 0
    private var foregroundTime: Long = 0
    var isInForeground: Boolean = false
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtil.d(TAG, "Activity created: ${activity.javaClass.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            // 进入前台
            isInForeground = true
            foregroundTime = System.currentTimeMillis()
            LogUtil.d(TAG, "App in foreground")
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            // 进入后台
            isInForeground = false
            val duration = System.currentTimeMillis() - foregroundTime
            LogUtil.d(TAG, "App went to background, foreground duration: ${duration}ms")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        LogUtil.d(TAG, "Activity destroyed: ${activity.javaClass.simpleName}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        LogUtil.d(TAG, "Configuration changed: ${newConfig.uiMode}")
    }

    override fun onLowMemory() {
        LogUtil.w(TAG, "Low memory warning")
    }
}
