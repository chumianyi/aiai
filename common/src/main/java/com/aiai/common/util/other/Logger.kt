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

import android.util.Log

/**
 * 分级日志工具类。
 *
 * 支持 VERBOSE / DEBUG / INFO / WARN / ERROR / ASSERT 六级输出。
 * 可全局开关。
 */
object Logger {

    var enabled: Boolean = true
    var tag: String = "AiAi"

    fun v(msg: String) { if (enabled) Log.v(tag, msg) }
    fun d(msg: String) { if (enabled) Log.d(tag, msg) }
    fun i(msg: String) { if (enabled) Log.i(tag, msg) }
    fun w(msg: String) { if (enabled) Log.w(tag, msg) }
    fun e(msg: String) { if (enabled) Log.e(tag, msg) }
    fun e(msg: String, tr: Throwable) { if (enabled) Log.e(tag, msg, tr) }
    fun wtf(msg: String) { if (enabled) Log.wtf(tag, msg) }
}
