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

import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener

/**
 * SSE事件监听器接口。
 *
 * 用于接收Server-Sent Events的事件回调。
 */
interface SseListener {

    /**
     * 连接成功。
     *
     * @param eventSource EventSource实例
     * @param response 响应对象
     */
    fun onOpen(eventSource: EventSource, response: Response)

    /**
     * 收到事件数据。
     *
     * @param id 事件ID
     * @param type 事件类型
     * @param data 事件数据
     */
    fun onEvent(id: String?, type: String?, data: String)

    /**
     * 连接关闭。
     */
    fun onClosed()

    /**
     * 发生错误。
     *
     * @param t 异常
     * @param response 响应对象（可能为null）
     */
    fun onError(t: Throwable, response: Response?)
}
