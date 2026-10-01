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
 * 消息缓存管理器
 *
 * 管理聊天消息的内存缓存，提供快速访问和LRU淘汰。
 */
class MessageCacheManager(private val context: Context) {

    private val maxCacheSize = 200
    private val cache = LinkedHashMap<String, MutableList<ChatMessage>>()

    private val _cacheSize = MutableStateFlow(0)
    val cacheSize: StateFlow<Int> = _cacheSize.asStateFlow()

    /**
     * 缓存会话的消息列表
     */
    fun cacheMessages(conversationId: String, messages: List<ChatMessage>) {
        if (cache.containsKey(conversationId)) {
            cache[conversationId]?.clear()
            cache[conversationId]?.addAll(messages)
        } else {
            cache[conversationId] = messages.toMutableList()
        }
        _cacheSize.value = cache.size
        evictIfNeeded()
    }

    /**
     * 获取缓存的消息
     */
    fun getCachedMessages(conversationId: String): List<ChatMessage>? {
        return cache[conversationId]
    }

    /**
     * 添加单条消息到缓存
     */
    fun addMessage(conversationId: String, message: ChatMessage) {
        val list = cache.getOrPut(conversationId) { mutableListOf() }
        list.add(message)
    }

    /**
     * 更新缓存中的消息
     */
    fun updateMessage(conversationId: String, message: ChatMessage) {
        val list = cache[conversationId] ?: return
        val index = list.indexOfFirst { it.id == message.id }
        if (index >= 0) {
            list[index] = message
        }
    }

    /**
     * 删除缓存中的消息
     */
    fun removeMessage(conversationId: String, messageId: String) {
        cache[conversationId]?.removeAll { it.id == messageId }
    }

    /**
     * 清除指定会话的缓存
     */
    fun clearConversationCache(conversationId: String) {
        cache.remove(conversationId)
        _cacheSize.value = cache.size
    }

    /**
     * 清除所有缓存
     */
    fun clearAll() {
        cache.clear()
        _cacheSize.value = 0
    }

    /**
     * 检查是否已缓存
     */
    fun isCached(conversationId: String): Boolean {
        return cache.containsKey(conversationId)
    }

    private fun evictIfNeeded() {
        while (cache.size > maxCacheSize) {
            val oldestKey = cache.keys.firstOrNull() ?: break
            cache.remove(oldestKey)
        }
    }
}
