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
package com.aiai.network.api

import com.aiai.network.model.request.AudioRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.AudioResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * 音频API服务接口。
 *
 * 提供语音识别（STT）和语音合成（TTS）功能。
 *
 * 接口列表：
 * - POST /audio/tts - 语音合成（文字转语音）
 * - POST /audio/stt - 语音识别（语音转文字）
 * - GET /audio/voices - 获取可用语音列表
 */
interface AudioApiService {

    /**
     * 语音合成（TTS）：将文本转换为语音。
     *
     * @param request 音频请求（包含文本、语音、速度等参数）
     * @return 音频响应（包含音频URL）
     */
    @POST("audio/tts")
    suspend fun textToSpeech(@Body request: AudioRequest): ApiResponse<AudioResponse>

    /**
     * 语音识别（STT）：将语音转换为文字。
     *
     * @param file 音频文件
     * @param language 识别语言
     * @return 识别结果
     */
    @Multipart
    @POST("audio/stt")
    suspend fun speechToText(
        @Part file: MultipartBody.Part,
        @Query("language") language: String = "zh-CN",
    ): ApiResponse<AudioResponse>

    /**
     * 获取可用语音列表。
     *
     * @param language 语言筛选
     * @return 语音列表
     */
    @POST("audio/voices")
    suspend fun getVoices(
        @Query("language") language: String = "zh-CN",
    ): ApiResponse<List<Map<String, Any>>>

    /**
     * 流式语音合成。
     *
     * @param request 音频请求
     * @return 流式音频数据
     */
    @POST("audio/tts/stream")
    suspend fun streamTts(@Body request: AudioRequest): okhttp3.ResponseBody

    /**
     * 流式语音识别。
     *
     * @param file 音频文件分片
     * @param language 识别语言
     * @return 实时识别结果
     */
    @Multipart
    @POST("audio/stt/stream")
    suspend fun streamStt(
        @Part file: MultipartBody.Part,
        @Query("language") language: String = "zh-CN",
    ): ApiResponse<AudioResponse>

    /**
     * 语音翻译。
     *
     * @param file 音频文件
     * @param sourceLanguage 源语言
     * @param targetLanguage 目标语言
     * @return 翻译结果
     */
    @Multipart
    @POST("audio/translate")
    suspend fun translateAudio(
        @Part file: MultipartBody.Part,
        @Query("source_language") sourceLanguage: String,
        @Query("target_language") targetLanguage: String,
    ): ApiResponse<AudioResponse>

    /**
     * 获取TTS配置。
     *
     * @return TTS配置信息
     */
    @POST("audio/tts/config")
    suspend fun getTtsConfig(): ApiResponse<Map<String, Any>>

    /**
     * 保存语音偏好。
     *
     * @param voiceId 语音ID
     * @param speed 语速
     * @param pitch 音调
     * @return 操作结果
     */
    @POST("audio/voice/preference")
    suspend fun saveVoicePreference(
        @Query("voice_id") voiceId: String,
        @Query("speed") speed: Float = 1.0f,
        @Query("pitch") pitch: Float = 1.0f,
    ): ApiResponse<Unit>
}
