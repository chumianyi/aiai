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
package com.aiai.common.util.other

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap

/**
 * 防抖工具类。
 *
 * 在指定时间内连续触发只执行最后一次。
 */
object DebounceUtil {

    private val handler = Handler(Looper.getMainLooper())
    private var runnables = ArrayMap<String, Runnable>()

    /**
     * 防抖执行。
     *
     * @param key 防抖键
     * @param delay 延迟（毫秒）
     * @param action 执行体
     */
    fun debounce(key: String, delay: Long = 300L, action: () -> Unit) {
        runnables[key]?.let { handler.removeCallbacks(it) }
        val runnable = Runnable { action() }
        runnables[key] = runnable
        handler.postDelayed(runnable, delay)
    }

    /** 取消指定 key 的防抖任务。 */
    fun cancel(key: String) {
        runnables[key]?.let { handler.removeCallbacks(it) }
        runnables.remove(key)
    }
}
