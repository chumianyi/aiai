/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.aiai.common.util.other.Logger

/**
 * 应用前后台切换监听器。
 *
 * 通过 ActivityLifecycleCallbacks 统计 Activity 数量，判断前后台。
 */
class AppLifecycleObserver : Application.ActivityLifecycleCallbacks {

    private var foregroundCount = 0
    var isForeground = false
        private set

    var onEnterForeground: (() -> Unit)? = null
    var onEnterBackground: (() -> Unit)? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Logger.d("Activity created: ${activity.javaClass.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        foregroundCount++
        if (!isForeground) {
            isForeground = true
            onEnterForeground?.invoke()
            Logger.d("App entered foreground")
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        foregroundCount--
        if (foregroundCount <= 0) {
            isForeground = false
            onEnterBackground?.invoke()
            Logger.d("App entered background")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        Logger.d("Activity destroyed: ${activity.javaClass.simpleName}")
    }
}
