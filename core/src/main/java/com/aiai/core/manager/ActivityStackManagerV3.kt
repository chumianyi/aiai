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

/**
 * Activity 栈 V3。
 */
object ActivityStackManagerV3 : Application.ActivityLifecycleCallbacks {
    private val stack = mutableListOf<Activity>()
    fun add(a: Activity) { stack.add(a) }
    fun remove(a: Activity) { stack.remove(a) }
    fun finishAll() { stack.forEach { it.finish() }; stack.clear() }
    override fun onActivityCreated(a: Activity, b: Bundle?) { add(a) }
    override fun onActivityStarted(a: Activity) {}
    override fun onActivityResumed(a: Activity) {}
    override fun onActivityPaused(a: Activity) {}
    override fun onActivityStopped(a: Activity) {}
    override fun onActivitySaveInstanceState(a: Activity, b: Bundle) {}
    override fun onActivityDestroyed(a: Activity) { remove(a) }
}
