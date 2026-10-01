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
package com.aiai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aiai.data.entity.UserSettingEntity
import kotlinx.coroutines.flow.Flow

/**
 * 用户设置DAO接口（单例模式）。
 */
@Dao
interface UserSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: UserSettingEntity)

    @Query("SELECT * FROM user_settings WHERE id = 1")
    fun getSettings(): Flow<UserSettingEntity?>

    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getSettingsSync(): UserSettingEntity?

    @Query("UPDATE user_settings SET themeMode = :themeMode WHERE id = 1")
    suspend fun updateThemeMode(themeMode: String)

    @Query("UPDATE user_settings SET language = :language WHERE id = 1")
    suspend fun updateLanguage(language: String)

    @Query("UPDATE user_settings SET fontScale = :fontScale WHERE id = 1")
    suspend fun updateFontScale(fontScale: Float)

    @Query("UPDATE user_settings SET markdownEnabled = :enabled WHERE id = 1")
    suspend fun updateMarkdownEnabled(enabled: Boolean)

    @Query("UPDATE user_settings SET notificationEnabled = :enabled WHERE id = 1")
    suspend fun updateNotificationEnabled(enabled: Boolean)

    @Query("UPDATE user_settings SET voiceInputEnabled = :enabled WHERE id = 1")
    suspend fun updateVoiceInputEnabled(enabled: Boolean)

    @Query("DELETE FROM user_settings")
    suspend fun clearAll()
}
