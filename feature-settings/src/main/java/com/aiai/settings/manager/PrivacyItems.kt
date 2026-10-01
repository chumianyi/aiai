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
 * 隐私数据项说明。
 */
data class PrivacyItem(
    val name: String,
    val desc: String,
    val collected: Boolean
)

object PrivacyItems {
    fun all(): List<PrivacyItem> = listOf(
        PrivacyItem("使用统计", "匿名功能使用数据", true),
        PrivacyItem("崩溃日志", "崩溃堆栈信息", true),
        PrivacyItem("设备信息", "机型/系统版本", false),
        PrivacyItem("位置信息", "用于本地化推荐", false),
        PrivacyItem("聊天记录", "仅存储本地", true)
    )
}
