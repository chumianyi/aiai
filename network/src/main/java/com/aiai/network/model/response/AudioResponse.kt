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
 * 音频响应模型。
 *
 * @property id 音频ID
 * @property audioUrl 音频文件URL
 * @property text 识别文本（STT结果）
 * @property durationMs 音频时长（毫秒）
 * @property format 音频格式
 * @property confidence 识别置信度（STT）
 * @property sampleRate 采样率
 * @property voice 使用的语音
 */
data class AudioResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("audio_url")
    val audioUrl: String = "",

    @SerializedName("text")
    val text: String = "",

    @SerializedName("duration_ms")
    val durationMs: Long = 0L,

    @SerializedName("format")
    val format: String = "mp3",

    @SerializedName("confidence")
    val confidence: Double = 0.0,

    @SerializedName("sample_rate")
    val sampleRate: Int = 22050,

    @SerializedName("voice")
    val voice: String = "default",
)
