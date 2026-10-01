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
import com.aiai.settings.model.ExportFormat

/**
 * 导出格式偏好记忆。
 */
class ExportPrefsManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_export", Context.MODE_PRIVATE)

    var lastFormat: ExportFormat
        get() = ExportFormat.fromExtension(prefs.getString("fmt", "txt") ?: "txt")
        set(value) = prefs.edit().putString("fmt", value.extension).apply()

    var lastDir: String
        get() = prefs.getString("dir", "") ?: ""
        set(value) = prefs.edit().putString("dir", value).apply()
}
