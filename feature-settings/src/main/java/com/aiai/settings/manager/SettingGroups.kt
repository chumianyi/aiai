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

/**
 * 设置项分组数据。
 */
data class SettingGroup(
    val title: String,
    val items: List<SettingState>
)

object SettingGroups {
    fun defaultGroups(): List<SettingGroup> = listOf(
        SettingGroup("AI 服务", listOf(
            SettingState("api_config", "API 配置", "接口地址与密钥", SettingType.NAVIGATION),
            SettingState("key_mgmt", "密钥管理", "加密存储", SettingType.NAVIGATION)
        )),
        SettingGroup("界面", listOf(
            SettingState("dark_mode", "深色模式", "", SettingType.SELECTOR),
            SettingState("font_size", "字体大小", "", SettingType.SELECTOR),
            SettingState("bubble", "气泡样式", "", SettingType.SELECTOR),
            SettingState("anim", "动画", "", SettingType.SWITCH)
        )),
        SettingGroup("通用", listOf(
            SettingState("language", "语言", "", SettingType.SELECTOR),
            SettingState("notification", "通知", "", SettingType.SWITCH),
            SettingState("auto_update", "自动更新", "", SettingType.SWITCH)
        ))
    )
}
