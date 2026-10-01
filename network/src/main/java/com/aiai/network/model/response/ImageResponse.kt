/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
 * 图片生成响应模型。
 *
 * @property id 图片ID
 * @property url 图片URL
 * @property b64Json Base64编码的图片数据
 * @property revisedPrompt 优化后的提示词
 * @property created 创建时间戳
 * @property size 图片尺寸
 * @property model 使用的模型
 */
data class ImageResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("b64_json")
    val b64Json: String = "",

    @SerializedName("revised_prompt")
    val revisedPrompt: String = "",

    @SerializedName("created")
    val created: Long = 0L,

    @SerializedName("size")
    val size: String = "1024x1024",

    @SerializedName("model")
    val model: String = "dall-e-3",
) {
    /**
     * 是否有图片URL。
     *
     * @return true如果URL非空
     */
    fun hasUrl(): Boolean = url.isNotBlank()

    /**
     * 是否有Base64数据。
     *
     * @return true如果b64Json非空
     */
    fun hasB64(): Boolean = b64Json.isNotBlank()
}
