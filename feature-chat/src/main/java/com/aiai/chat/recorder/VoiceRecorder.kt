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
package com.aiai.chat.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 语音录制器
 *
 * 实现语音录制功能，支持：
 * - 开始/停止/取消录制
 * - 实时音量采集（用于波形显示）
 * - 录制时长限制
 * - 自动保存到缓存目录
 */
class VoiceRecorder(private val context: Context) {

    private val TAG = "VoiceRecorder"
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var isRecording: Boolean = false
    private var startTime: Long = 0L

    var onMaxDurationReached: (() -> Unit)? = null
    var onAmplitudeListener: ((amplitude: Int) -> Unit)? = null

    var maxDurationMs: Long = 60_000L // 最大60秒

    /**
     * 开始录制
     *
     * @return 是否成功开始
     */
    fun startRecording(): Boolean {
        if (isRecording) {
            Log.w(TAG, "Already recording")
            return false
        }

        return try {
            outputFile = createOutputFile()
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(44100)
                setAudioChannels(1)
                setAudioEncodingBitRate(128000)
                setOutputFile(outputFile?.absolutePath)
                prepare()
                start()
            }
            startTime = System.currentTimeMillis()
            isRecording = true
            Log.d(TAG, "Recording started: ${outputFile?.absolutePath}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Start recording failed: ${e.message}")
            isRecording = false
            false
        }
    }

    /**
     * 停止录制
     *
     * @return 录制的文件，失败返回null
     */
    fun stopRecording(): File? {
        if (!isRecording) return null

        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            Log.d(TAG, "Recording stopped: ${outputFile?.absolutePath}")
            outputFile
        } catch (e: Exception) {
            Log.e(TAG, "Stop recording failed: ${e.message}")
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
            outputFile?.delete()
            null
        }
    }

    /**
     * 取消录制并删除文件
     */
    fun cancelRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.stop()
                mediaRecorder?.release()
            } catch (e: Exception) {
                Log.e(TAG, "Cancel stop error: ${e.message}")
            }
            mediaRecorder = null
            isRecording = false
            outputFile?.delete()
            outputFile = null
            Log.d(TAG, "Recording cancelled")
        }
    }

    /**
     * 是否正在录制
     */
    fun isRecording(): Boolean = isRecording

    /**
     * 获取已录制时长（毫秒）
     */
    fun getAmplitude(): Int {
        return if (isRecording) {
            mediaRecorder?.maxAmplitude ?: 0
        } else 0
    }

    /**
     * 获取录制时长
     */
    fun getRecordDuration(): Long {
        return if (isRecording) {
            System.currentTimeMillis() - startTime
        } else 0
    }

    /**
     * 创建输出文件
     */
    private fun createOutputFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = File(context.cacheDir, "voice_records").apply { mkdirs() }
        return File(dir, "voice_${timeStamp}.m4a")
    }

    /**
     * 释放资源
     */
    fun release() {
        if (isRecording) {
            cancelRecording()
        }
    }
}
