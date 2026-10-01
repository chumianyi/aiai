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
 * 音频请求模型。
 *
 * 用于TTS（文字转语音）和STT（语音转文字）的API请求。
 *
 * @property text 要合成的文本（TTS用）
 * @property voice 语音ID
 * @property speed 语速（0.5-2.0）
 * @property pitch 音调（0.5-2.0）
 * @property format 输出格式（mp3/wav/pcm）
 * @property sampleRate 采样率
 * @property language 语言
 * @property emotion 情感风格
 */
data class AudioRequest(
    @SerializedName("text")
    val text: String = "",

    @SerializedName("voice")
    val voice: String = "default",

    @SerializedName("speed")
    val speed: Float = 1.0f,

    @SerializedName("pitch")
    val pitch: Float = 1.0f,

    @SerializedName("format")
    val format: String = "mp3",

    @SerializedName("sample_rate")
    val sampleRate: Int = 22050,

    @SerializedName("language")
    val language: String = "zh-CN",

    @SerializedName("emotion")
    val emotion: String = "neutral",
) {
    init {
        require(speed in 0.5f..2.0f) { "speed must be between 0.5 and 2.0" }
        require(pitch in 0.5f..2.0f) { "pitch must be between 0.5 and 2.0" }
        require(format in listOf("mp3", "wav", "pcm", "ogg")) {
            "format must be mp3, wav, pcm, or ogg"
        }
    }
}
