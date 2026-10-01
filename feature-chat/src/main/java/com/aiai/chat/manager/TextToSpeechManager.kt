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
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息朗读管理器
 *
 * 使用TTS将AI回复朗读出来。
 */
class TextToSpeechManager(private val context: Context) {

    enum class TtsState {
        IDLE,
        INITIALIZING,
        SPEAKING,
        PAUSED,
        ERROR
    }

    data class TtsConfig(
        val speechRate: Float = 1.0f,
        val pitch: Float = 1.0f,
        val language: String = "zh-CN"
    )

    private val _ttsState = MutableStateFlow(TtsState.IDLE)
    val ttsState: StateFlow<TtsState> = _ttsState.asStateFlow()

    private val _config = MutableStateFlow(TtsConfig())
    val config: StateFlow<TtsConfig> = _config.asStateFlow()

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    fun speak(text: String) {
        _ttsState.value = TtsState.SPEAKING
        _currentText.value = text
    }

    fun stop() {
        _ttsState.value = TtsState.IDLE
        _currentText.value = ""
    }

    fun pause() {
        _ttsState.value = TtsState.PAUSED
    }

    fun resume() {
        _ttsState.value = TtsState.SPEAKING
    }

    fun updateConfig(config: TtsConfig) {
        _config.value = config
    }

    fun isSpeaking(): Boolean {
        return _ttsState.value == TtsState.SPEAKING
    }
}
