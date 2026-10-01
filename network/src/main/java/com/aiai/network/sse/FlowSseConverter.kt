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

import com.aiai.network.model.response.StreamResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Response
import okhttp3.sse.EventSource

/**
 * SSE转Flow转换器，将SSE事件流转换为Kotlin Flow。
 *
 * 使用方式：
 * ```
 * val flow = FlowSseConverter().convert(sseClient, url, bodyJson)
 * flow.collect { streamResponse -> ... }
 * ```
 */
class FlowSseConverter {

    private val parser = SseParser()

    /**
     * 将SSE请求转换为Flow。
     *
     * @param sseClient SSE客户端
     * @param url 请求URL
     * @param bodyJson 请求体JSON
     * @return StreamResponse的Flow
     */
    fun convert(
        sseClient: SseClient,
        url: String,
        bodyJson: String,
    ): Flow<StreamResponse> = callbackFlow {
        val listener = object : SseListener {
            override fun onOpen(eventSource: EventSource, response: Response) {
                // 连接成功，无需处理
            }

            override fun onEvent(id: String?, type: String?, data: String) {
                val parsed = parser.parse(data)
                if (parsed != null) {
                    trySend(parsed)
                } else {
                    // [DONE] 结束标记
                    close()
                }
            }

            override fun onClosed() {
                close()
            }

            override fun onError(t: Throwable, response: Response?) {
                close(t)
            }
        }

        sseClient.stream(url, bodyJson, listener)

        awaitClose {
            sseClient.cancel()
        }
    }
}
