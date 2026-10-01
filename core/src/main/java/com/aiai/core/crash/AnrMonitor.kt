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
package com.aiai.core.crash

import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * ANR 监测器。
 *
 * 基于主线程消息循环，检测主线程阻塞超过阈值。
 */
object AnrMonitor {

    private const val TAG = "AnrMonitor"
    private const val ANR_THRESHOLD = 5000L // 5秒

    private val mainHandler = Handler(Looper.getMainLooper())
    private var tickRunnable: Runnable? = null

    /** 开始监测。 */
    fun start() {
        tickRunnable = object : Runnable {
            override fun run() {
                val start = System.currentTimeMillis()
                mainHandler.postDelayed(this, 1000)
                val dispatch = System.currentTimeMillis() - start
                // 如果 postDelayed 的 runnable 延迟超过阈值，说明主线程阻塞
                if (dispatch > ANR_THRESHOLD) {
                    Log.w(TAG, "Potential ANR detected: main thread blocked for ${dispatch}ms")
                }
            }
        }
        mainHandler.post(tickRunnable!!)
    }

    /** 停止监测。 */
    fun stop() {
        tickRunnable?.let { mainHandler.removeCallbacks(it) }
    }
}
