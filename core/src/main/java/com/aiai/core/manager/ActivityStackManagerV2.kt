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
package com.aiai.core.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.aiai.common.util.other.Logger

/**
 * Activity 栈管理器 V2。
 */
object ActivityStackManagerV2 : Application.ActivityLifecycleCallbacks {

    private val stack = mutableListOf<Activity>()

    fun add(activity: Activity) { stack.add(activity) }
    fun remove(activity: Activity) { stack.remove(activity) }
    fun top(): Activity? = if (stack.isNotEmpty()) stack.last() else null
    fun finishAll() { stack.forEach { it.finish() }; stack.clear() }
    fun size(): Int = stack.size

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { add(activity) }
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) { remove(activity) }
}
