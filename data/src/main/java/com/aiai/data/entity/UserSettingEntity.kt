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
package com.aiai.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户设置表实体。
 *
 * 存储用户的所有偏好设置，单例模式（id固定为1）。
 *
 * @property id 设置ID（固定为1）
 * @property themeMode 主题模式（light/dark/system）
 * @property language 界面语言
 * @property fontScale 字体缩放比例
 * @property bubbleStyle 气泡样式
 * @property autoScroll 自动滚动
 * @property sendOnEnter 回车发送
 * @property markdownEnabled Markdown渲染
 * @property codeHighlight 代码高亮
 * @property voiceInputEnabled 语音输入
 * @property imageGenerationEnabled 图片生成开关
 * @property notificationEnabled 通知开关
 * @property cacheSizeLimit 缓存大小限制（MB）
 */
@Entity(tableName = "user_settings")
data class UserSettingEntity(
    @PrimaryKey
    val id: Int = 1,
    val themeMode: String = "system",
    val language: String = "zh-CN",
    val fontScale: Float = 1.0f,
    val bubbleStyle: String = "modern",
    val autoScroll: Boolean = true,
    val sendOnEnter: Boolean = true,
    val markdownEnabled: Boolean = true,
    val codeHighlight: Boolean = true,
    val voiceInputEnabled: Boolean = true,
    val imageGenerationEnabled: Boolean = true,
    val notificationEnabled: Boolean = true,
    val cacheSizeLimit: Int = 100,
)
