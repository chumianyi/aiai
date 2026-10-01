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
package com.aiai.settings.manager

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.aiai.settings.model.AppLanguage
import java.util.Locale

/**
 * 语言管理。
 *
 * 支持 中 / 英 / 日 / 韩 / 跟随系统，通过生成带 Locale 的 [Configuration]
 * 包装 Context，实现应用内语言切换而无需重启系统级语言。
 */
class LanguageManager(private val settings: SettingsManager) {

    /** 当前选中的语言。 */
    val current: AppLanguage get() = settings.language.value

    /**
     * 设置语言并持久化。
     */
    fun setLanguage(language: AppLanguage) {
        settings.setLanguage(language)
    }

    /**
     * 基于 [base] 生成应用了当前语言的 ContextWrapper。
     * 在 Activity.attachBaseContext 中调用。
     */
    fun attach(base: Context): Context {
        val lang = current
        if (lang == AppLanguage.SYSTEM) return base
        val locale = Locale(lang.tag.split("-").first(), lang.tag.split("-").getOrNull(1) ?: "")
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    /**
     * 获取当前展示用的 Locale。
     */
    fun currentLocale(): Locale {
        val tag = current.tag
        if (tag.isEmpty()) return Locale.getDefault()
        val parts = tag.split("-")
        return Locale(parts.first(), parts.getOrNull(1) ?: "")
    }

    companion object {
        @Volatile
        private var instance: LanguageManager? = null

        fun get(context: Context): LanguageManager {
            return instance ?: synchronized(this) {
                instance ?: LanguageManager(SettingsManager.get(context)).also { instance = it }
            }
        }
    }
}
