/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aiai.settings.manager.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 语音设置 UI 状态。 */
data class VoiceSettingsUiState(
    val engineIndex: Int = 0,
    val toneIndex: Int = 0,
    val speed: Float = 1.0f,
    val autoPlay: Boolean = false,
    val shortcut: String = "长按空格键"
)

/** 语音识别引擎候选。 */
val VOICE_ENGINES = listOf("系统语音识别", "百度语音", "讯飞语音", "Whisper")

/** 音色候选。 */
val VOICE_TONES = listOf("温柔女声", "沉稳男声", "活泼少女", "磁性大叔", "萝莉音")

/**
 * 语音设置 ViewModel。
 */
class VoiceSettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsManager.get(app)

    private val _uiState = MutableStateFlow(
        VoiceSettingsUiState(
            engineIndex = settings.getInt("voice_engine", 0),
            toneIndex = settings.getInt("voice_tone", 0),
            speed = settings.getFloat("voice_speed", 1.0f),
            autoPlay = settings.voiceAutoPlay.value,
            shortcut = settings.getString("voice_shortcut", "长按空格键")
        )
    )
    val uiState: StateFlow<VoiceSettingsUiState> = _uiState.asStateFlow()

    fun setEngine(index: Int) {
        settings.putInt("voice_engine", index)
        _uiState.value = _uiState.value.copy(engineIndex = index)
    }

    fun setTone(index: Int) {
        settings.putInt("voice_tone", index)
        _uiState.value = _uiState.value.copy(toneIndex = index)
    }

    fun setSpeed(speed: Float) {
        settings.putFloat("voice_speed", speed)
        _uiState.value = _uiState.value.copy(speed = speed)
    }

    fun setAutoPlay(enabled: Boolean) {
        settings.setVoiceAutoPlay(enabled)
        _uiState.value = _uiState.value.copy(autoPlay = enabled)
    }

    fun setShortcut(text: String) {
        settings.putString("voice_shortcut", text)
        _uiState.value = _uiState.value.copy(shortcut = text)
    }
}
