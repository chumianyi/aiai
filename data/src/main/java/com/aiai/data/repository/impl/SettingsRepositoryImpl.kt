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
package com.aiai.data.repository.impl

import com.aiai.data.dao.UserSettingDao
import com.aiai.data.entity.UserSettingEntity
import com.aiai.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

/** 设置仓库实现 */
class SettingsRepositoryImpl(
    private val userSettingDao: UserSettingDao,
) : SettingsRepository {

    override fun getSettings(): Flow<UserSettingEntity?> = userSettingDao.getSettings()
    override suspend fun getSettingsSync(): UserSettingEntity? = userSettingDao.getSettingsSync()
    override suspend fun saveSettings(settings: UserSettingEntity) = userSettingDao.insert(settings)
    override suspend fun updateThemeMode(mode: String) = userSettingDao.updateThemeMode(mode)
    override suspend fun updateLanguage(language: String) = userSettingDao.updateLanguage(language)
    override suspend fun updateFontScale(scale: Float) = userSettingDao.updateFontScale(scale)
    override suspend fun updateMarkdownEnabled(enabled: Boolean) = userSettingDao.updateMarkdownEnabled(enabled)
    override suspend fun updateNotificationEnabled(enabled: Boolean) = userSettingDao.updateNotificationEnabled(enabled)
    override suspend fun updateVoiceInputEnabled(enabled: Boolean) = userSettingDao.updateVoiceInputEnabled(enabled)
    override suspend fun clearSettings() = userSettingDao.clearAll()
}
