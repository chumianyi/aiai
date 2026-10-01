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
 * 通知渠道列表。
 */
object NotificationChannels {

    data class Channel(val id: String, val name: String, val desc: String)

    fun all(): List<Channel> = listOf(
        Channel("default", "默认通知", "一般消息"),
        Channel("chat", "新消息", "新聊天提醒"),
        Channel("update", "更新", "版本更新提醒"),
        Channel("promo", "推广", "活动通知")
    )
}
