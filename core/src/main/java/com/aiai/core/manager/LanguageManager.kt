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
package com.aiai.core.manager

import android.content.Context
import android.content.res.Configuration
import com.aiai.common.util.storage.MMKVManager
import java.util.Locale

/**
 * 多语言管理器。
 *
 * 支持：跟随系统 / 中文 / 英文。
 */
object LanguageManager {

    const val SYSTEM = "system"
    const val ZH = "zh"
    const val EN = "en"

    var language: String
        get() = MMKVManager.getString("language", SYSTEM)
        set(value) = MMKVManager.putString("language", value)

    /** 更新 Configuration。 */
    fun attachBaseContext(context: Context): Context {
        val locale = when (language) {
            ZH -> Locale.SIMPLIFIED_CHINESE
            EN -> Locale.ENGLISH
            else -> Locale.getDefault()
        }
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
