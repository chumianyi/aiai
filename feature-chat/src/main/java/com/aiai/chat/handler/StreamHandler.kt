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
package com.aiai.chat.handler

import android.util.Log
import com.aiai.chat.data.model.StreamChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 流式响应处理器
 *
 * 处理SSE（Server-Sent Events）流式响应，解析增量数据，
 * 拼接完整消息内容，管理流式接收状态。
 * 支持取消、重试、错误处理。
 */
class StreamHandler(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
) {

    private val TAG = "StreamHandler"
    private var eventSource: EventSource? = null
    private var currentJob: Job? = null
    private var isCancelled = false

    /**
     * 发起流式请求
     *
     * @param url API端点URL
     * @param requestBody 请求体JSON
     * @param onChunk 接收到增量数据回调
     * @param onComplete 完成回调
     * @param onError 错误回调
     */
    fun startStream(
        url: String,
        requestBody: String,
        onChunk: (StreamChunk) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        isCancelled = false

        val request = Request.Builder()
            .url(url)
            .addHeader("Accept", "text/event-stream")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toRequestBody())
            .build()

        val listener = object : EventSourceListener() {
            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                if (isCancelled) return
                val chunk = parseSseData(data)
                chunk?.let { onChunk(it) }
            }

            override fun onClosed(eventSource: EventSource) {
                Log.d(TAG, "Stream closed")
                onComplete()
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                if (isCancelled) return
                val errorMsg = t?.message ?: response?.message ?: "Unknown error"
                Log.e(TAG, "Stream failure: $errorMsg")
                onError(errorMsg)
            }
        }

        val factory = EventSources.createFactory(client)
        eventSource = factory.newEventSource(request, listener)
    }

    /**
     * 取消流式请求
     */
    fun cancel() {
        isCancelled = true
        eventSource?.cancel()
        currentJob?.cancel()
        Log.d(TAG, "Stream cancelled")
    }

    /**
     * 解析SSE数据行
     */
    private fun parseSseData(data: String): StreamChunk? {
        return try {
            if (data.trim() == "[DONE]") {
                StreamChunk(isFinished = true, finishReason = "stop")
            } else {
                val json = JSONObject(data)
                val choices = json.optJSONArray("choices")
                if (choices != null && choices.length() > 0) {
                    val choice = choices.getJSONObject(0)
                    val delta = choice.optJSONObject("delta")
                    val content = delta?.optString("content") ?: ""
                    val finishReason = choice.optString("finishReason", null)
                    StreamChunk(
                        deltaContent = content,
                        isFinished = finishReason != null,
                        finishReason = finishReason
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Parse SSE error: ${e.message}")
            null
        }
    }

    /**
     * 以Flow形式收集流式数据
     */
    fun streamAsFlow(
        url: String,
        requestBody: String
    ): Flow<StreamChunk> = flow {
        val onChunk: (StreamChunk) -> Unit = { chunk -> emit(chunk) }
        val onComplete: () -> Unit = { }
        val onError: (String) -> Unit = { error ->
            emit(StreamChunk(error = error))
        }
        startStream(url, requestBody, onChunk, onComplete, onError)
    }.flowOn(Dispatchers.IO)

    private fun String.toRequestBody(): okhttp3.RequestBody {
        return okhttp3.RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            this
        )
    }
}
