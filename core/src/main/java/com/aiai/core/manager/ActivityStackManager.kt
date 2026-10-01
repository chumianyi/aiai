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
import java.lang.ref.WeakReference
import java.util.Stack

/**
 * Activity 栈管理器。
 *
 * 管理所有 Activity，支持 finish 指定、finish 到指定、退出应用。
 */
object ActivityStackManager {

    private val stack = Stack<WeakReference<Activity>>()

    /** 添加 Activity 到栈。 */
    fun add(activity: Activity) {
        stack.push(WeakReference(activity))
    }

    /** 从栈移除 Activity。 */
    fun remove(activity: Activity) {
        stack.removeAll { it.get() === activity }
    }

    /** 获取栈顶 Activity。 */
    fun topActivity(): Activity? {
        return stack.lastOrNull()?.get()
    }

    /** finish 到指定 Activity（含目标）。 */
    fun finishTo(clazz: Class<*>) {
        val iterator = stack.reversed().iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next().get() ?: continue
            if (activity.javaClass == clazz) break
            activity.finish()
            iterator.remove()
        }
    }

    /** finish 所有 Activity。 */
    fun finishAll() {
        stack.forEach { it.get()?.finish() }
        stack.clear()
    }

    /** 退出应用。 */
    fun exitApp() {
        finishAll()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /** 栈大小。 */
    fun size(): Int = stack.size
}
