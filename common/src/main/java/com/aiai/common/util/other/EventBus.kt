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
package com.aiai.common.util.other

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 轻量级事件总线（基于 SharedFlow）。
 *
 * 支持粘性事件、多订阅者。
 */
object EventBus {

    private val _events = MutableSharedFlow<Any>(extraBufferCapacity = 64)
    val events: SharedFlow<Any> = _events.asSharedFlow()

    /** 发送事件。 */
    suspend fun post(event: Any) {
        _events.emit(event)
    }

    /** 发送事件（非挂起，tryEmit）。 */
    fun postSync(event: Any) {
        _events.tryEmit(event)
    }
}
