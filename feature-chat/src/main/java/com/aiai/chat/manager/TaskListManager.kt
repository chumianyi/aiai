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
 * 消息任务列表管理器
 *
 * 管理消息中的任务列表（Checklist）显示。
 */
class TaskListManager(private val context: Context) {

    data class TaskItem(
        val id: String,
        val text: String,
        val checked: Boolean = false
    )

    private val _tasks = MutableStateFlow<Map<String, List<TaskItem>>>(emptyMap())
    val tasks: StateFlow<Map<String, List<TaskItem>>> = _tasks.asStateFlow()

    fun setTasks(messageId: String, tasks: List<TaskItem>) {
        _tasks.value = _tasks.value + (messageId to tasks)
    }

    fun toggleTask(messageId: String, taskId: String) {
        val current = _tasks.value[messageId] ?: return
        val updated = current.map {
            if (it.id == taskId) it.copy(checked = !it.checked) else it
        }
        _tasks.value = _tasks.value + (messageId to updated)
    }

    fun getTasks(messageId: String): List<TaskItem> {
        return _tasks.value[messageId] ?: emptyList()
    }

    fun getCompletedCount(messageId: String): Int {
        return _tasks.value[messageId]?.count { it.checked } ?: 0
    }

    fun getTotalCount(messageId: String): Int {
        return _tasks.value[messageId]?.size ?: 0
    }
}
