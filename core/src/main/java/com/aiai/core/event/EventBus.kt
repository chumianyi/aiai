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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.event

import com.aiai.common.model.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 基于 Flow 的事件总线。
 *
 * 支持粘性事件、线程切换、生命周期感知。
 */
object EventBus {

    private val _events = MutableSharedFlow<Event>(
        extraBufferCapacity = 64,
        replay = 1 // 支持粘性事件（最新一个事件）
    )
    val events: SharedFlow<Event> = _events.asSharedFlow()

    /** 发送事件。 */
    suspend fun post(event: Event) {
        _events.emit(event)
    }

    /** 发送事件（非挂起）。 */
    fun postSync(event: Event) {
        _events.tryEmit(event)
    }

    /** 过滤特定类型的事件。 */
    inline fun <reified T : Event> filter(): kotlinx.coroutines.flow.Flow<T> {
        return events.filterIsInstance()
    }

    private fun <T> kotlinx.coroutines.flow.Flow<Event>.filterIsInstance(): kotlinx.coroutines.flow.Flow<T> {
        return kotlinx.coroutines.flow.filter { it is T }.let { it as kotlinx.coroutines.flow.Flow<T> }
    }
}
