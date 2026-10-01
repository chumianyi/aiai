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
 * 反馈管理器
 *
 * 管理用户反馈的提交和历史。
 */
class FeedbackManager(private val context: Context) {

    data class Feedback(
        val id: String,
        val type: FeedbackType,
        val content: String,
        val contact: String = "",
        val timestamp: Long = System.currentTimeMillis(),
        val status: FeedbackStatus = FeedbackStatus.PENDING
    )

    enum class FeedbackType(val displayName: String) {
        BUG("问题反馈"),
        FEATURE("功能建议"),
        COMPLAINT("投诉"),
        OTHER("其他")
    }

    enum class FeedbackStatus(val displayName: String) {
        PENDING("待处理"),
        PROCESSING("处理中"),
        RESOLVED("已解决"),
        CLOSED("已关闭")
    }

    private val _feedbacks = MutableStateFlow<List<Feedback>>(emptyList())
    val feedbacks: StateFlow<List<Feedback>> = _feedbacks.asStateFlow()

    fun submitFeedback(type: FeedbackType, content: String, contact: String): Feedback {
        val feedback = Feedback(
            id = "fb_${System.currentTimeMillis()}",
            type = type,
            content = content,
            contact = contact
        )
        _feedbacks.value = _feedbacks.value + feedback
        return feedback
    }

    fun getFeedbackById(id: String): Feedback? {
        return _feedbacks.value.find { it.id == id }
    }

    fun updateFeedbackStatus(id: String, status: FeedbackStatus) {
        _feedbacks.value = _feedbacks.value.map {
            if (it.id == id) it.copy(status = status) else it
        }
    }
}
