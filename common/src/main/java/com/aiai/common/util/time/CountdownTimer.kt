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
package com.aiai.common.util.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 倒计时器。
 *
 * 支持设置总时长、倒计时间隔、完成回调、暂停/继续/取消。
 */
class CountdownTimer(
    private val totalMillis: Long,
    private val intervalMillis: Long = 1000L,
    private val onTick: (remainingMillis: Long) -> Unit = {},
    private val onFinish: () -> Unit = {}
) {
    private var job: Job? = null
    private var remaining: Long = totalMillis
    private var paused: Boolean = false

    /** 启动倒计时。 */
    fun start() {
        job?.cancel()
        remaining = totalMillis
        paused = false
        job = CoroutineScope(Dispatchers.Main).launch {
            while (remaining > 0 && !paused) {
                delay(intervalMillis)
                remaining -= intervalMillis
                onTick(remaining.coerceAtLeast(0))
            }
            if (remaining <= 0) onFinish()
        }
    }

    /** 暂停。 */
    fun pause() { paused = true }

    /** 继续。 */
    fun resume() {
        if (paused) {
            paused = false
            job = CoroutineScope(Dispatchers.Main).launch {
                while (remaining > 0 && !paused) {
                    delay(intervalMillis)
                    remaining -= intervalMillis
                    onTick(remaining.coerceAtLeast(0))
                }
                if (remaining <= 0) onFinish()
            }
        }
    }

    /** 取消。 */
    fun cancel() {
        job?.cancel()
        remaining = 0
    }
}
