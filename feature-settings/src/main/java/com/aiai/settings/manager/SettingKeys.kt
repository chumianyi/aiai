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
import com.aiai.settings.model.BubbleStyle
import com.aiai.settings.model.FontScale
import com.aiai.settings.model.ThemeMode

/**
 * 设置项元数据：定义所有可配置项的 key、默认值、类型、分组。
 * 便于统一渲染设置列表。
 */
object SettingKeys {

    // 主题
    const val THEME_MODE = "theme_mode"
    const val THEME_COLOR = "theme_color"
    const val FONT_SCALE = "font_scale"
    const val BUBBLE_STYLE = "bubble_style"
    const val ANIM_ENABLED = "anim_enabled"

    // 通用
    const val LANGUAGE = "language"
    const val NOTIFICATION = "notification_enabled"
    const val AUTO_UPDATE = "auto_update"
    const val SAVE_CHAT = "save_chat_history"
    const val DATA_COLLECTION = "data_collection"
    const val SEND_METHOD = "send_method"
    const val DEFAULT_TEMP = "default_temperature"
    const val DEFAULT_MAX_TOKENS = "default_max_tokens"

    // 语音
    const val VOICE_ENGINE = "voice_engine"
    const val VOICE_TONE = "voice_tone"
    const val VOICE_SPEED = "voice_speed"
    const val VOICE_AUTO_PLAY = "voice_auto_play"
    const val VOICE_SHORTCUT = "voice_shortcut"

    // 图片生成
    const val IMG_SIZE = "img_size"
    const val IMG_STYLE = "img_style"
    const val IMG_COUNT = "img_count"
    const val IMG_PATH = "img_path"
    const val IMG_WATERMARK = "img_watermark"

    // 默认值
    val DEFAULT_THEME_MODE = ThemeMode.SYSTEM
    val DEFAULT_FONT_SCALE = FontScale.MEDIUM
    val DEFAULT_BUBBLE_STYLE = BubbleStyle.ROUNDED
    val DEFAULT_LANGUAGE = AppLanguage.SYSTEM
    const val DEFAULT_TEMPERATURE = 0.7f
    const val DEFAULT_MAX_TOKENS = 2048
    const val DEFAULT_SEND_METHOD = 0

    /** 设置项分组定义。 */
    enum class Group(val title: String) {
        AI_SERVICE("AI 服务"),
        APPEARANCE("界面"),
        GENERAL("通用"),
        VOICE("语音"),
        IMAGE("图片生成"),
        DATA("数据"),
        ABOUT("关于")
    }
}
