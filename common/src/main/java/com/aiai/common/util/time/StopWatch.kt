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

/**
 * 秒表计时器。
 *
 * 支持 start / pause / resume / reset / lap（计次）。
 */
class StopWatch {

    private var startTime: Long = 0L
    private var pausedTime: Long = 0L
    private var running: Boolean = false
    private val laps = mutableListOf<Long>()

    /** 启动。 */
    fun start() {
        startTime = System.nanoTime()
        pausedTime = 0L
        running = true
        laps.clear()
    }

    /** 暂停。 */
    fun pause() {
        if (running) {
            pausedTime = System.nanoTime() - startTime
            running = false
        }
    }

    /** 继续。 */
    fun resume() {
        if (!running && pausedTime > 0) {
            startTime = System.nanoTime() - pausedTime
            pausedTime = 0L
            running = true
        }
    }

    /** 重置。 */
    fun reset() {
        startTime = 0L
        pausedTime = 0L
        running = false
        laps.clear()
    }

    /** 计次，返回当前累计时长（毫秒）。 */
    fun lap(): Long {
        val now = elapsedMillis()
        laps.add(now)
        return now
    }

    /** 获取已计次列表。 */
    fun getLaps(): List<Long> = laps.toList()

    /** 获取当前累计时长（毫秒）。 */
    fun elapsedMillis(): Long {
        return if (running) {
            (System.nanoTime() - startTime) / 1_000_000
        } else {
            pausedTime / 1_000_000
        }
    }

    /** 是否正在运行。 */
    fun isRunning(): Boolean = running
}
