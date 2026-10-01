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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息语音播放管理器
 *
 * 管理聊天中语音消息的播放状态。
 */
class VoicePlaybackManager(private val context: Context) {

    data class PlaybackState(
        val messageId: String = "",
        val isPlaying: Boolean = false,
        val progress: Float = 0f,
        val duration: Long = 0L
    )

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    fun play(messageId: String, duration: Long) {
        _playbackState.value = PlaybackState(messageId, true, 0f, duration)
    }

    fun pause() {
        _playbackState.value = _playbackState.value.copy(isPlaying = false)
    }

    fun stop() {
        _playbackState.value = PlaybackState()
    }

    fun updateProgress(progress: Float) {
        _playbackState.value = _playbackState.value.copy(progress = progress)
    }

    fun isPlaying(messageId: String): Boolean {
        return _playbackState.value.isPlaying && _playbackState.value.messageId == messageId
    }
}
