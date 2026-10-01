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

import android.os.SystemClock
import android.util.ArrayMap

/**
 * 节流工具类。
 *
 * 在指定时间间隔内只执行一次。
 */
object ThrottleUtil {

    private var lastTimes = ArrayMap<String, Long>()

    /**
     * 执行节流操作。
     *
     * @param key 节流键（区分不同操作）
     * @param interval 间隔（毫秒）
     * @param action 执行体
     */
    fun throttle(key: String, interval: Long = 500L, action: () -> Unit) {
        val now = SystemClock.elapsedRealtime()
        val last = lastTimes[key] ?: 0L
        if (now - last >= interval) {
            lastTimes[key] = now
            action()
        }
    }
}
