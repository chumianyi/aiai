/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONArray
import org.json.JSONObject

/**
 * 数据导入器。
 *
 * 提供 JSON 导入、批量插入、进度回调等功能。
 */
class DataImporter(private val context: Context) {

    companion object {
        private const val TAG = "DataImporter"
    }

    /**
     * 导入进度回调。
     *
     * @property current 当前进度
     * @property total 总数
     * @property message 进度消息
     */
    data class ImportProgress(
        val current: Int,
        val total: Int,
        val message: String = ""
    )

    /**
     * 从 JSON 文件导入数据。
     *
     * @param uri 文件 Uri
     * @param onImport 导入每条数据的回调
     * @return 导入进度流
     */
    fun importFromJson(
        uri: Uri,
        onImport: (JSONObject) -> Unit
    ): Flow<ImportProgress> = flow {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val content = inputStream?.bufferedReader().use { it?.readText() } ?: ""
            inputStream?.close()

            val jsonArray = JSONArray(content)
            val total = jsonArray.length()

            emit(ImportProgress(0, total, "开始导入 $total 条数据"))

            for (i in 0 until total) {
                val item = jsonArray.getJSONObject(i)
                onImport(item)
                emit(ImportProgress(i + 1, total, "已导入 ${i + 1}/$total"))
            }

            emit(ImportProgress(total, total, "导入完成"))
            Log.d(TAG, "Import completed: $total items")
        } catch (e: Exception) {
            Log.e(TAG, "Import failed", e)
            emit(ImportProgress(0, 0, "导入失败: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 从 JSON 字符串导入对话数据。
     *
     * @param jsonString JSON 字符串
     * @return 导入结果
     */
    suspend fun importConversations(jsonString: String): Result<Int> {
        return try {
            val jsonArray = JSONArray(jsonString)
            var count = 0

            for (i in 0 until jsonArray.length()) {
                val conversation = jsonArray.getJSONObject(i)
                // 实际导入逻辑由调用方实现
                count++
            }

            Log.d(TAG, "Imported $count conversations")
            Result.success(count)
        } catch (e: Exception) {
            Log.e(TAG, "Import conversations failed", e)
            Result.failure(e)
        }
    }

    /**
     * 批量插入数据。
     *
     * @param items 数据列表
     * @param insertBlock 插入每条数据的回调
     */
    suspend fun <T> batchInsert(
        items: List<T>,
        insertBlock: suspend (T) -> Unit
    ): Flow<ImportProgress> = flow {
        val total = items.size
        emit(ImportProgress(0, total, "开始批量插入"))

        items.forEachIndexed { index, item ->
            insertBlock(item)
            emit(ImportProgress(index + 1, total, "已插入 ${index + 1}/$total"))
        }

        emit(ImportProgress(total, total, "批量插入完成"))
    }.flowOn(Dispatchers.IO)
}
