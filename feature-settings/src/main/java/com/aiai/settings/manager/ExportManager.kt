/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.ExportConversation
import com.aiai.settings.model.ExportFormat
import com.aiai.settings.model.ExportProgress
import com.aiai.settings.model.ExportState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 导出管理。
 *
 * 负责把用户选中的会话列表按指定格式导出为文件，提供进度回调，
 * 底层使用 [ChatExporter] 完成格式转换与文件写入。
 */
class ExportManager(
    private val context: Context,
    private val exporter: ChatExporter = ChatExporter()
) {

    private val _progress = MutableStateFlow(ExportProgress())
    /** 导出进度流。 */
    val progress: StateFlow<ExportProgress> = _progress.asStateFlow()

    private var cancelled = false

    /** 取消当前导出任务。 */
    fun cancel() {
        cancelled = true
    }

    /**
     * 导出 [conversations] 到 [format]，输出目录默认 app 外部 filesDir/exports。
     * @return 生成的文件列表
     */
    suspend fun export(
        conversations: List<ExportConversation>,
        format: ExportFormat
    ): List<File> = withContext(Dispatchers.IO) {
        cancelled = false
        val outDir = File(context.getExternalFilesDir(null), "exports").apply { mkdirs() }
        val results = mutableListOf<File>()
        _progress.value = ExportProgress(total = conversations.size, state = ExportState.RUNNING)

        conversations.forEachIndexed { index, conv ->
            if (cancelled) {
                _progress.value = _progress.value.copy(state = ExportState.CANCELLED)
                return@withContext results
            }
            _progress.value = ExportProgress(
                total = conversations.size,
                processed = index,
                fileName = conv.title,
                state = ExportState.RUNNING
            )
            try {
                val file = exporter.exportConversation(conv, format, outDir)
                results.add(file)
            } catch (e: Exception) {
                // 单条失败不中断整体
            }
            _progress.value = _progress.value.copy(processed = index + 1)
        }
        _progress.value = _progress.value.copy(
            processed = conversations.size,
            state = if (cancelled) ExportState.CANCELLED else ExportState.SUCCESS
        )
        results
    }

    /** 导出目录。 */
    fun exportDir(): File =
        File(context.getExternalFilesDir(null), "exports").apply { mkdirs() }

    companion object {
        @Volatile
        private var instance: ExportManager? = null

        fun get(context: Context): ExportManager {
            return instance ?: synchronized(this) {
                instance ?: ExportManager(context.applicationContext).also { instance = it }
            }
        }

        /** 时间戳格式化。 */
        fun timestampName(): String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    }
}
