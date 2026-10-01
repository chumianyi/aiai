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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 用户设置请求模型。
 *
 * 用于更新用户偏好设置。
 *
 * @property themeMode 主题模式（light/dark/system）
 * @property language 界面语言
 * @property fontScale 字体缩放比例
 * @property bubbleStyle 气泡样式
 * @property autoScroll 自动滚动
 * @property sendOnEnter 回车发送
 * @property markdownEnabled Markdown渲染
 * @property codeHighlight 代码高亮
 * @property voiceInputEnabled 语音输入
 * @property imageGenerationEnabled 图片生成
 * @property notificationEnabled 通知开关
 * @property cacheSizeLimit 缓存大小限制（MB）
 */
data class UserSettingRequest(
    @SerializedName("theme_mode")
    val themeMode: String = "system",

    @SerializedName("language")
    val language: String = "zh-CN",

    @SerializedName("font_scale")
    val fontScale: Float = 1.0f,

    @SerializedName("bubble_style")
    val bubbleStyle: String = "modern",

    @SerializedName("auto_scroll")
    val autoScroll: Boolean = true,

    @SerializedName("send_on_enter")
    val sendOnEnter: Boolean = true,

    @SerializedName("markdown_enabled")
    val markdownEnabled: Boolean = true,

    @SerializedName("code_highlight")
    val codeHighlight: Boolean = true,

    @SerializedName("voice_input_enabled")
    val voiceInputEnabled: Boolean = true,

    @SerializedName("image_generation_enabled")
    val imageGenerationEnabled: Boolean = true,

    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean = true,

    @SerializedName("cache_size_limit")
    val cacheSizeLimit: Int = 100,
) {
    init {
        require(themeMode in listOf("light", "dark", "system")) {
            "theme_mode must be light, dark, or system"
        }
        require(fontScale in 0.8f..1.5f) { "font_scale must be between 0.8 and 1.5" }
        require(cacheSizeLimit > 0) { "cache_size_limit must be positive" }
    }
}
