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
package com.aiai.core.log

import android.util.Log

/**
 * 日志 V3。
 */
object LoggerV3 {
    var enabled = true
    var tag = "AiAi"

    fun d(msg: String) { if (enabled) Log.d(tag, msg) }
    fun i(msg: String) { if (enabled) Log.i(tag, msg) }
    fun w(msg: String) { if (enabled) Log.w(tag, msg) }
    fun e(msg: String) { if (enabled) Log.e(tag, msg) }
}
