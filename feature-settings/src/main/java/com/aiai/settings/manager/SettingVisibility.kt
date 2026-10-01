/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 设置项可见性过滤。
 */
object SettingVisibility {

    fun shouldShow(key: String, isDebug: Boolean): Boolean {
        if (isDebug) return true
        return when (key) {
            "debug_log" -> false
            "lab_options" -> false
            else -> true
        }
    }
}
