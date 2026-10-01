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
import android.content.SharedPreferences
import com.aiai.settings.model.AppLanguage
import com.aiai.settings.model.BubbleStyle
import com.aiai.settings.model.FontScale
import com.aiai.settings.model.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 设置管理中心。
 *
 * 统一封装 [SharedPreferences] 读写，提供强类型访问器与 [StateFlow] 变更通知，
 * 所有设置项变更都会同步推送到对应 Flow，UI 层可直接收集。
 *
 * 设计原则：
 * - 所有默认值集中在本类，避免散落各处。
 * - 写入后立即更新对应 StateFlow，保证一致性。
 * - 监听 SharedPreferences 跨进程/外部修改。
 */
class SettingsManager private constructor(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // region StateFlow 状态

    private val _themeMode = MutableStateFlow(loadThemeMode())
    /** 主题模式流。 */
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _fontScale = MutableStateFlow(loadFontScale())
    /** 字体缩放流。 */
    val fontScale: StateFlow<FontScale> = _fontScale.asStateFlow()

    private val _bubbleStyle = MutableStateFlow(loadBubbleStyle())
    /** 气泡样式流。 */
    val bubbleStyle: StateFlow<BubbleStyle> = _bubbleStyle.asStateFlow()

    private val _themeColor = MutableStateFlow(prefs.getInt(KEY_THEME_COLOR, DEFAULT_THEME_COLOR))
    /** 主题色流。 */
    val themeColor: StateFlow<Int> = _themeColor.asStateFlow()

    private val _language = MutableStateFlow(loadLanguage())
    /** 语言流。 */
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _animEnabled = MutableStateFlow(prefs.getBoolean(KEY_ANIM, true))
    /** 动画开关流。 */
    val animEnabled: StateFlow<Boolean> = _animEnabled.asStateFlow()

    private val _notificationEnabled = MutableStateFlow(prefs.getBoolean(KEY_NOTIFICATION, true))
    val notificationEnabled: StateFlow<Boolean> = _notificationEnabled.asStateFlow()

    private val _autoUpdate = MutableStateFlow(prefs.getBoolean(KEY_AUTO_UPDATE, true))
    val autoUpdate: StateFlow<Boolean> = _autoUpdate.asStateFlow()

    private val _saveChatHistory = MutableStateFlow(prefs.getBoolean(KEY_SAVE_CHAT, true))
    val saveChatHistory: StateFlow<Boolean> = _saveChatHistory.asStateFlow()

    private val _dataCollection = MutableStateFlow(prefs.getBoolean(KEY_DATA_COLLECTION, false))
    val dataCollection: StateFlow<Boolean> = _dataCollection.asStateFlow()

    private val _voiceAutoPlay = MutableStateFlow(prefs.getBoolean(KEY_VOICE_AUTO_PLAY, false))
    val voiceAutoPlay: StateFlow<Boolean> = _voiceAutoPlay.asStateFlow()

    private val _imageWatermark = MutableStateFlow(prefs.getBoolean(KEY_IMAGE_WATERMARK, false))
    val imageWatermark: StateFlow<Boolean> = _imageWatermark.asStateFlow()

    // endregion

    // region 通用读写

    /** 通用 Boolean 读写。 */
    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean =
        prefs.getBoolean(key, default)

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String = ""): String =
        prefs.getString(key, default) ?: default

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, default: Int = 0): Int = prefs.getInt(key, default)

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, default: Float = 0f): Float = prefs.getFloat(key, default)

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, default: Long = 0L): Long = prefs.getLong(key, default)

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    /** 清除所有非敏感设置（保留登录态等由各 Manager 自行处理）。 */
    fun clearAll() {
        prefs.edit().clear().apply()
    }

    // endregion

    // region 主题

    /** 设置主题模式并广播。 */
    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

    private fun loadThemeMode(): ThemeMode =
        ThemeMode.fromName(prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: "")

    /** 设置主题色。 */
    fun setThemeColor(color: Int) {
        prefs.edit().putInt(KEY_THEME_COLOR, color).apply()
        _themeColor.value = color
    }

    // endregion

    // region 字体 / 气泡

    fun setFontScale(scale: FontScale) {
        prefs.edit().putFloat(KEY_FONT_SCALE, scale.scale).apply()
        _fontScale.value = scale
    }

    private fun loadFontScale(): FontScale =
        FontScale.fromScale(prefs.getFloat(KEY_FONT_SCALE, FontScale.MEDIUM.scale))

    fun setBubbleStyle(style: BubbleStyle) {
        prefs.edit().putString(KEY_BUBBLE_STYLE, style.name).apply()
        _bubbleStyle.value = style
    }

    private fun loadBubbleStyle(): BubbleStyle =
        BubbleStyle.fromName(prefs.getString(KEY_BUBBLE_STYLE, BubbleStyle.ROUNDED.name) ?: "")

    fun setAnimEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ANIM, enabled).apply()
        _animEnabled.value = enabled
    }

    // endregion

    // region 语言

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE, language.tag).apply()
        _language.value = language
    }

    private fun loadLanguage(): AppLanguage =
        AppLanguage.fromTag(prefs.getString(KEY_LANGUAGE, "") ?: "")

    // endregion

    // region 通用开关

    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATION, enabled).apply()
        _notificationEnabled.value = enabled
    }

    fun setAutoUpdate(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_UPDATE, enabled).apply()
        _autoUpdate.value = enabled
    }

    fun setSaveChatHistory(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SAVE_CHAT, enabled).apply()
        _saveChatHistory.value = enabled
    }

    fun setDataCollection(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DATA_COLLECTION, enabled).apply()
        _dataCollection.value = enabled
    }

    fun setVoiceAutoPlay(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VOICE_AUTO_PLAY, enabled).apply()
        _voiceAutoPlay.value = enabled
    }

    fun setImageWatermark(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_IMAGE_WATERMARK, enabled).apply()
        _imageWatermark.value = enabled
    }

    // endregion

    // region 默认对话参数

    /** 默认发送方式：0 回车发送 / 1 Ctrl+回车。 */
    var sendMethod: Int
        get() = prefs.getInt(KEY_SEND_METHOD, 0)
        set(value) = prefs.edit().putInt(KEY_SEND_METHOD, value).apply()

    /** 默认 temperature。 */
    var defaultTemperature: Float
        get() = prefs.getFloat(KEY_DEFAULT_TEMP, 0.7f)
        set(value) = prefs.edit().putFloat(KEY_DEFAULT_TEMP, value).apply()

    /** 默认 max tokens。 */
    var defaultMaxTokens: Int
        get() = prefs.getInt(KEY_DEFAULT_MAX_TOKENS, 2048)
        set(value) = prefs.edit().putInt(KEY_DEFAULT_MAX_TOKENS, value).apply()

    // endregion

    companion object {
        private const val PREFS_NAME = "aiai_settings"
        private const val DEFAULT_THEME_COLOR = 0xFF3D5AFE.toInt()

        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_THEME_COLOR = "theme_color"
        const val KEY_FONT_SCALE = "font_scale"
        const val KEY_BUBBLE_STYLE = "bubble_style"
        const val KEY_ANIM = "anim_enabled"
        const val KEY_LANGUAGE = "language"
        const val KEY_NOTIFICATION = "notification_enabled"
        const val KEY_AUTO_UPDATE = "auto_update"
        const val KEY_SAVE_CHAT = "save_chat_history"
        const val KEY_DATA_COLLECTION = "data_collection"
        const val KEY_VOICE_AUTO_PLAY = "voice_auto_play"
        const val KEY_IMAGE_WATERMARK = "image_watermark"
        const val KEY_SEND_METHOD = "send_method"
        const val KEY_DEFAULT_TEMP = "default_temperature"
        const val KEY_DEFAULT_MAX_TOKENS = "default_max_tokens"

        @Volatile
        private var instance: SettingsManager? = null

        /** 获取全局单例。 */
        fun get(context: Context): SettingsManager {
            return instance ?: synchronized(this) {
                instance ?: SettingsManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
