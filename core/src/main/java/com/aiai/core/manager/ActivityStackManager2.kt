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
import java.util.Stack

/**
 * Activity 栈管理器。
 *
 * 维护全局 Activity 栈，支持：
 * - 添加/移除 Activity
 * - 获取栈顶 Activity
 * - 关闭指定 Activity
 * - 关闭到指定 Activity
 * - 关闭所有 Activity
 * - 退出应用
 */
object ActivityStackManager : Application.ActivityLifecycleCallbacks {

    private val activityStack = Stack<Activity>()

    /** 添加 Activity 到栈顶。 */
    fun add(activity: Activity) {
        activityStack.push(activity)
        Logger.d("Activity pushed: ${activity.javaClass.simpleName}, stack size: ${activityStack.size}")
    }

    /** 从栈中移除 Activity。 */
    fun remove(activity: Activity) {
        activityStack.remove(activity)
        Logger.d("Activity removed: ${activity.javaClass.simpleName}, stack size: ${activityStack.size}")
    }

    /** 获取栈顶 Activity。 */
    fun topActivity(): Activity? {
        return if (activityStack.isNotEmpty()) activityStack.lastElement() else null
    }

    /** 获取栈底 Activity。 */
    fun bottomActivity(): Activity? {
        return if (activityStack.isNotEmpty()) activityStack.firstElement() else null
    }

    /** 关闭指定类名的 Activity。 */
    fun finishActivity(clazz: Class<*>) {
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity.javaClass == clazz) {
                activity.finish()
                iterator.remove()
            }
        }
    }

    /** 关闭到指定类名的 Activity（保留目标）。 */
    fun finishToActivity(clazz: Class<*>) {
        val iterator = activityStack.reversed().iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity.javaClass == clazz) break
            activity.finish()
            iterator.remove()
        }
    }

    /** 关闭所有 Activity。 */
    fun finishAll() {
        activityStack.forEach { it.finish() }
        activityStack.clear()
    }

    /** 退出应用。 */
    fun exitApp() {
        finishAll()
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    /** 栈大小。 */
    fun size(): Int = activityStack.size

    /** 是否为空。 */
    fun isEmpty(): Boolean = activityStack.isEmpty()

    // ===== ActivityLifecycleCallbacks =====

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        add(activity)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        remove(activity)
    }
}
