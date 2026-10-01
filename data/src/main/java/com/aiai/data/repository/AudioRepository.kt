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
package com.aiai.data.repository

import com.aiai.network.model.request.AudioRequest
import com.aiai.network.model.response.AudioResponse
import java.io.File

/** 音频仓库接口 */
interface AudioRepository {
    suspend fun textToSpeech(request: AudioRequest): Result<AudioResponse>
    suspend fun speechToText(file: File): Result<AudioResponse>
    suspend fun getVoices(language: String): Result<List<Map<String, Any>>>
    suspend fun saveVoicePreference(voiceId: String, speed: Float, pitch: Float): Result<Unit>
    suspend fun getTtsConfig(): Result<Map<String, Any>>
    suspend fun streamTts(request: AudioRequest): Result<String>
}
