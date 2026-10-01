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
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.common.model

/**
 * 可消费事件包装类。
 *
 * 包装一次性事件，避免 Configuration Change 后重复消费。
 *
 * @param T 事件数据类型
 * @property content 事件内容
 */
class ConsumableEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    /** 获取事件内容（仅在未被消费时返回）。 */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /** 获取事件内容（无论是否已消费）。 */
    fun peekContent(): T = content

    /** 是否已被消费。 */
    fun isConsumed(): Boolean = hasBeenHandled
}
