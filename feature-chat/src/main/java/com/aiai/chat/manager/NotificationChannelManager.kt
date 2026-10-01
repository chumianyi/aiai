/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 通知渠道管理器
 *
 * 管理Android通知渠道的创建和配置。
 */
class NotificationChannelManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID_MESSAGE = "channel_message"
        const val CHANNEL_ID_GENERAL = "channel_general"
        const val CHANNEL_ID_PROMOTION = "channel_promotion"
    }

    data class ChannelInfo(
        val id: String,
        val name: String,
        val description: String,
        val importance: Int
    )

    private val _channels = MutableStateFlow<List<ChannelInfo>>(emptyList())
    val channels: StateFlow<List<ChannelInfo>> = _channels.asStateFlow()

    init {
        createDefaultChannels()
    }

    private fun createDefaultChannels() {
        _channels.value = listOf(
            ChannelInfo(CHANNEL_ID_MESSAGE, "消息通知", "接收新消息通知", android.app.NotificationManager.IMPORTANCE_HIGH),
            ChannelInfo(CHANNEL_ID_GENERAL, "通用通知", "应用通用通知", android.app.NotificationManager.IMPORTANCE_DEFAULT),
            ChannelInfo(CHANNEL_ID_PROMOTION, "推广通知", "活动和推广通知", android.app.NotificationManager.IMPORTANCE_LOW)
        )
    }

    fun getChannelInfo(channelId: String): ChannelInfo? {
        return _channels.value.find { it.id == channelId }
    }

    fun getAllChannels(): List<ChannelInfo> {
        return _channels.value
    }
}
