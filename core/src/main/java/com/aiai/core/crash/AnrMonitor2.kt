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

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.aiai.common.util.other.Logger

/**
 * ANR 监测器。
 */
object AnrMonitor {

    private lateinit var context: Context
    private val handler = Handler(Looper.getMainLooper())
    private var tick = 0L

    fun install(context: Context) {
        this.context = context.applicationContext
        handler.postDelayed(object : Runnable {
            override fun run() {
                tick = System.currentTimeMillis()
                handler.postDelayed(this, 5000)
            }
        }, 5000)
        Logger.d("AnrMonitor installed")
    }

    /** 检查是否 ANR。 */
    fun checkAnr(): Boolean {
        return System.currentTimeMillis() - tick > 10000
    }
}
