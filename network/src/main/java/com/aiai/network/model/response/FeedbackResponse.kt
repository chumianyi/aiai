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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 反馈响应模型集合。
 *
 * 包含反馈提交、列表、回复等反馈相关响应数据结构。
 */
sealed class FeedbackResponse {

    /**
     * 基础响应。
     */
    open class BaseResponse(
        @SerializedName("code")
        open val code: Int = 0,
        @SerializedName("message")
        open val message: String = ""
    )

    /**
     * 反馈数据。
     *
     * @property feedbackId 反馈ID
     * @property title 标题
     * @property content 内容
     * @property type 类型
     * @property images 图片列表
     * @property status 状态
     * @property createdAt 创建时间
     * @property updatedAt 更新时间
     */
    data class Feedback(
        @SerializedName("feedback_id")
        val feedbackId: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("type")
        val type: String = "other",
        @SerializedName("images")
        val images: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "pending",
        @SerializedName("created_at")
        val createdAt: Long = 0,
        @SerializedName("updated_at")
        val updatedAt: Long = 0
    )

    /**
     * 反馈响应。
     */
    data class FeedbackResponse(
        @SerializedName("feedback")
        val feedback: Feedback = Feedback()
    ) : BaseResponse()

    /**
     * 反馈列表响应。
     *
     * @property list 反馈列表
     * @property total 总数
     * @property page 当前页
     * @property pageSize 每页数量
     */
    data class FeedbackListResponse(
        @SerializedName("list")
        val list: List<Feedback> = emptyList(),
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("page_size")
        val pageSize: Int = 20
    ) : BaseResponse()

    /**
     * 反馈回复数据。
     *
     * @property replyId 回复ID
     * @property feedbackId 反馈ID
     * @property content 回复内容
     * @property replier 回复人
     * @property createdAt 创建时间
     */
    data class Reply(
        @SerializedName("reply_id")
        val replyId: String = "",
        @SerializedName("feedback_id")
        val feedbackId: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("replier")
        val replier: String = "",
        @SerializedName("created_at")
        val createdAt: Long = 0
    )

    /**
     * 反馈详情响应。
     */
    data class FeedbackDetailResponse(
        @SerializedName("feedback")
        val feedback: Feedback = Feedback(),
        @SerializedName("replies")
        val replies: List<Reply> = emptyList()
    ) : BaseResponse()

    /**
     * 回复响应。
     */
    data class ReplyResponse(
        @SerializedName("reply")
        val reply: Reply = Reply()
    ) : BaseResponse()

    /**
     * 图片上传响应。
     *
     * @property url 图片URL
     * @property thumbnail 缩略图URL
     */
    data class UploadImageResponse(
        @SerializedName("url")
        val url: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = ""
    ) : BaseResponse()

    /**
     * 反馈类型。
     *
     * @property type 类型
     * @property name 名称
     * @property icon 图标
     */
    data class FeedbackType(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("icon")
        val icon: String = ""
    )

    /**
     * 反馈类型列表响应。
     */
    data class FeedbackTypeListResponse(
        @SerializedName("types")
        val types: List<FeedbackType> = emptyList()
    ) : BaseResponse()
}
