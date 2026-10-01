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
 * 导出文件名生成。
 */
object ExportFileNameGenerator {

    private val fmt = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())

    fun generate(sessionName: String, ext: String): String {
        val safe = sessionName.replace(Regex("[^\\w\\u4e00-\\u9fa5]"), "_")
        return "AiAi_${safe}_${fmt.format(System.currentTimeMillis())}.$ext"
    }
}
