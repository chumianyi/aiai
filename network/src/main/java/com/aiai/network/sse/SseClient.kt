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
package com.aiai.network.sse

import com.aiai.network.config.ApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

/**
 * SSE客户端，基于OkHttp EventSource实现Server-Sent Events流式请求。
 *
 * 功能：
 * - 发起SSE请求
 * - 接收流式数据
 * - 自动解析数据
 * - 支持取消请求
 *
 * @property config API配置
 */
class SseClient(
    private val config: ApiConfig = ApiConfig.getInstance(),
) {

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(config.connectTimeoutMs, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS) // SSE不限制读取超时
            .build()
    }

    private val parser = SseParser()
    private var eventSource: EventSource? = null

    /**
     * 发起SSE流式请求。
     *
     * @param url 请求URL
     * @param bodyJson 请求体JSON字符串
     * @param listener SSE事件监听器
     * @return EventSource实例
     */
    fun stream(
        url: String,
        bodyJson: String,
        listener: SseListener,
    ): EventSource {
        val request = Request.Builder()
            .url(url)
            .post(bodyJson.toRequestBody("application/json".toMediaType()))
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer ${config.apiKey}")
            .build()

        val factory = EventSources.createFactory(client)

        eventSource = factory.newEventSource(request, object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                listener.onOpen(eventSource, response)
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                listener.onEvent(id, type, data)
            }

            override fun onClosed(eventSource: EventSource) {
                listener.onClosed()
            }

            override fun onFailure(eventSource: EventSource, t: Throwable, response: Response?) {
                listener.onError(t, response)
            }
        })

        return eventSource!!
    }

    /**
     * 取消SSE请求。
     */
    fun cancel() {
        eventSource?.cancel()
        eventSource = null
    }
}
