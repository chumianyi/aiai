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
package com.aiai.chat.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

/**
 * 语音播放器
 *
 * 基于MediaPlayer实现语音消息播放，支持：
 * - 播放/暂停/停止
 * - 进度回调
 * - 播放完成监听
 * - 同时只播放一条语音
 */
class VoicePlayer(private val context: Context) {

    private val TAG = "VoicePlayer"
    private var mediaPlayer: MediaPlayer? = null
    private var currentPath: String? = null
    private var isPlaying: Boolean = false

    var onProgressListener: ((currentMs: Long, totalMs: Long) -> Unit)? = null
    var onCompletionListener: (() -> Unit)? = null
    var onStartListener: (() -> Unit)? = null

    /**
     * 播放语音文件
     */
    fun play(path: String) {
        stop()
        currentPath = path

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )

            try {
                setDataSource(path)
                prepare()
                setOnPreparedListener {
                    start()
                    isPlaying = true
                    onStartListener?.invoke()
                    Log.d(TAG, "Started playback: $path")
                }
                setOnCompletionListener {
                    isPlaying = false
                    onCompletionListener?.invoke()
                    Log.d(TAG, "Playback completed: $path")
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "Playback error: what=$what extra=$extra")
                    isPlaying = false
                    true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Playback failed: ${e.message}")
            }
        }
    }

    /**
     * 播放URI
     */
    fun playUri(uri: Uri) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            try {
                setDataSource(context, uri)
                prepare()
                start()
                isPlaying = true
                onStartListener?.invoke()
            } catch (e: Exception) {
                Log.e(TAG, "Playback URI failed: ${e.message}")
            }
        }
    }

    /**
     * 暂停播放
     */
    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                isPlaying = false
            }
        }
    }

    /**
     * 恢复播放
     */
    fun resume() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
                isPlaying = true
            }
        }
    }

    /**
     * 停止播放并释放资源
     */
    fun stop() {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            } catch (e: Exception) {
                Log.e(TAG, "Stop error: ${e.message}")
            }
        }
        mediaPlayer = null
        isPlaying = false
        currentPath = null
    }

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean = isPlaying

    /**
     * 获取当前播放位置
     */
    fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition ?: 0
    }

    /**
     * 获取总时长
     */
    fun getDuration(): Long {
        return mediaPlayer?.duration ?: 0
    }

    /**
     * 释放资源
     */
    fun release() {
        stop()
    }
}
