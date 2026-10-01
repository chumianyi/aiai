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
import com.aiai.settings.model.AppLanguage

/**
 * 语言列表提供者。
 */
object LanguageProvider {

    /** 所有可选语言。 */
    fun all(): List<AppLanguage> = AppLanguage.entries.toList()

    /** 根据标签查找。 */
    fun fromTag(tag: String): AppLanguage = AppLanguage.fromTag(tag)
}
