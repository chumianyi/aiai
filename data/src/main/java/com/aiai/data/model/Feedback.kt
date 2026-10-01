/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.data.model

/**
 * 反馈域模型。
 *
 * 表示用户提交的反馈及其回复。
 */
data class Feedback(
    val feedbackId: String,
    val title: String,
    val content: String,
    val type: FeedbackType,
    val images: List<String> = emptyList(),
    val status: FeedbackStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val replies: List<FeedbackReply> = emptyList()
)

/**
 * 反馈类型枚举。
 */
enum class FeedbackType(val displayName: String) {
    /** 功能建议。 */
    FEATURE("功能建议"),
    /** Bug 报告。 */
    BUG("Bug报告"),
    /** 使用咨询。 */
    CONSULTATION("使用咨询"),
    /** 其他。 */
    OTHER("其他")
}

/**
 * 反馈状态枚举。
 */
enum class FeedbackStatus(val displayName: String) {
    /** 待处理。 */
    PENDING("待处理"),
    /** 处理中。 */
    PROCESSING("处理中"),
    /** 已回复。 */
    REPLIED("已回复"),
    /** 已关闭。 */
    CLOSED("已关闭")
}

/**
 * 反馈回复。
 *
 * @property replyId 回复ID
 * @property content 回复内容
 * @property replier 回复人
 * @property createdAt 创建时间
 * @property isAdmin 是否为管理员回复
 */
data class FeedbackReply(
    val replyId: String,
    val content: String,
    val replier: String,
    val createdAt: Long,
    val isAdmin: Boolean = false
)
