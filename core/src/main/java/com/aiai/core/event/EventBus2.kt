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

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 基于 Flow 的事件总线。
 */
object EventBus2 {

    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    private val _stickyEvents = mutableMapOf<String, Event>()

    /** 发送事件。 */
    suspend fun post(event: Event) {
        _events.emit(event)
    }

    /** 发送粘性事件。 */
    suspend fun postSticky(event: Event) {
        _stickyEvents[event.javaClass.name] = event
        _events.emit(event)
    }

    /** 获取粘性事件。 */
    fun <T : Event> getSticky(clazz: Class<T>): T? {
        return _stickyEvents[clazz.name] as? T
    }

    /** 移除粘性事件。 */
    fun removeSticky(clazz: Class<*>) {
        _stickyEvents.remove(clazz.name)
    }

    /** 清除所有粘性事件。 */
    fun clearSticky() {
        _stickyEvents.clear()
    }
}
