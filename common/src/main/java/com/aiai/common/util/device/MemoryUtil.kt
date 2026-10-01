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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util.device

import android.app.ActivityManager
import android.content.Context

/**
 * 内存状态工具类。
 */
object MemoryUtil {

    /** 获取已用内存（字节）。 */
    fun getUsedMemory(): Long {
        val runtime = Runtime.getRuntime()
        return runtime.totalMemory() - runtime.freeMemory()
    }

    /** 获取总内存（字节）。 */
    fun getTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory()
    }

    /** 获取可用内存（字节）。 */
    fun getAvailableMemory(): Long {
        return Runtime.getRuntime().maxMemory() - getUsedMemory()
    }

    /** 获取系统内存信息。 */
    fun getSystemMemoryInfo(context: Context): ActivityManager.MemoryInfo {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        return info
    }

    /** 判断内存是否不足。 */
    fun isLowMemory(context: Context): Boolean {
        return getSystemMemoryInfo(context).lowMemory
    }
}
