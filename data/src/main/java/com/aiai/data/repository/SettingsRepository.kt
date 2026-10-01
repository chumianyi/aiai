/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.repository

import com.aiai.data.entity.UserSettingEntity
import kotlinx.coroutines.flow.Flow

/** 设置仓库接口 */
interface SettingsRepository {
    fun getSettings(): Flow<UserSettingEntity?>
    suspend fun getSettingsSync(): UserSettingEntity?
    suspend fun saveSettings(settings: UserSettingEntity)
    suspend fun updateThemeMode(mode: String)
    suspend fun updateLanguage(language: String)
    suspend fun updateFontScale(scale: Float)
    suspend fun updateMarkdownEnabled(enabled: Boolean)
    suspend fun updateNotificationEnabled(enabled: Boolean)
    suspend fun updateVoiceInputEnabled(enabled: Boolean)
    suspend fun clearSettings()
}
