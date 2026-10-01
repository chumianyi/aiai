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
package com.aiai.data.repository.impl

import com.aiai.data.repository.AudioRepository
import com.aiai.network.api.AudioApiService
import com.aiai.network.model.request.AudioRequest
import com.aiai.network.model.response.AudioResponse
import java.io.File

/** 音频仓库实现 */
class AudioRepositoryImpl(
    private val audioApi: AudioApiService,
) : AudioRepository {

    override suspend fun textToSpeech(request: AudioRequest): Result<AudioResponse> {
        return try {
            val response = audioApi.textToSpeech(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun speechToText(file: File): Result<AudioResponse> {
        return try {
            Result.success(AudioResponse())
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun getVoices(language: String): Result<List<Map<String, Any>>> {
        return try {
            val response = audioApi.getVoices(language)
            if (response.isSuccessful()) Result.success(response.data ?: emptyList())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun saveVoicePreference(voiceId: String, speed: Float, pitch: Float): Result<Unit> {
        return try {
            audioApi.saveVoicePreference(voiceId, speed, pitch)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun getTtsConfig(): Result<Map<String, Any>> {
        return try {
            val response = audioApi.getTtsConfig()
            if (response.isSuccessful()) Result.success(response.data ?: emptyMap())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun streamTts(request: AudioRequest): Result<String> {
        return try { Result.success("") } catch (e: Exception) { Result.failure(e) }
    }
}
