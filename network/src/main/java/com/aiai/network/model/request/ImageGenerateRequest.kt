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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 图片生成请求模型。
 *
 * 用于文生图、图生图、图片编辑的API请求。
 *
 * @property prompt 图片描述提示词
 * @property negativePrompt 负面提示词
 * @property model 模型名称
 * @property size 图片尺寸（如1024x1024）
 * @property n 生成图片数量
 * @property quality 质量标准（standard/hd）
 * @property style 风格（vivid/natural）
 * @property responseFormat 响应格式（url/b64_json）
 * @property referenceImage 参考图片URL（图生图用）
 */
data class ImageGenerateRequest(
    @SerializedName("prompt")
    val prompt: String = "",

    @SerializedName("negative_prompt")
    val negativePrompt: String = "",

    @SerializedName("model")
    val model: String = "dall-e-3",

    @SerializedName("size")
    val size: String = "1024x1024",

    @SerializedName("n")
    val n: Int = 1,

    @SerializedName("quality")
    val quality: String = "standard",

    @SerializedName("style")
    val style: String = "vivid",

    @SerializedName("response_format")
    val responseFormat: String = "url",

    @SerializedName("reference_image")
    val referenceImage: String = "",
) {
    init {
        require(prompt.isNotBlank()) { "prompt must not be blank" }
        require(n in 1..10) { "n must be between 1 and 10" }
        require(quality in listOf("standard", "hd")) {
            "quality must be standard or hd"
        }
        require(responseFormat in listOf("url", "b64_json")) {
            "response_format must be url or b64_json"
        }
    }
}
