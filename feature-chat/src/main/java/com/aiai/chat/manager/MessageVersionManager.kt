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
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息版本管理器
 *
 * 管理AI消息的多版本重生成对比。
 */
class MessageVersionManager(private val context: Context) {

    data class MessageVersion(
        val versionId: String,
        val messageId: String,
        val content: String,
        val timestamp: Long = System.currentTimeMillis(),
        val isCurrent: Boolean = false
    )

    private val _versions = MutableStateFlow<Map<String, List<MessageVersion>>>(emptyMap())
    val versions: StateFlow<Map<String, List<MessageVersion>>> = _versions.asStateFlow()

    fun addVersion(messageId: String, content: String) {
        val current = _versions.value[messageId] ?: emptyList()
        val newVersion = MessageVersion(
            versionId = "ver_${System.currentTimeMillis()}",
            messageId = messageId,
            content = content,
            isCurrent = true
        )
        // 将之前的版本标记为非当前
        val updated = current.map { it.copy(isCurrent = false) } + newVersion
        _versions.value = _versions.value + (messageId to updated)
    }

    fun getVersions(messageId: String): List<MessageVersion> {
        return _versions.value[messageId] ?: emptyList()
    }

    fun switchToVersion(messageId: String, versionId: String): MessageVersion? {
        val versions = _versions.value[messageId] ?: return null
        val targetVersion = versions.find { it.versionId == versionId } ?: return null
        val updated = versions.map { it.copy(isCurrent = it.versionId == versionId) }
        _versions.value = _versions.value + (messageId to updated)
        return targetVersion
    }

    fun getCurrentVersion(messageId: String): MessageVersion? {
        return _versions.value[messageId]?.find { it.isCurrent }
    }

    fun hasVersions(messageId: String): Boolean {
        return (_versions.value[messageId]?.size ?: 0) > 1
    }
}
