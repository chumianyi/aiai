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
package com.aiai.common.callback

/**
 * 初始化器接口。
 *
 * 用于模块化初始化，支持异步/延迟。
 */
interface Initializer {
    /** 是否在主线程初始化。 */
    val isMainThread: Boolean

    /** 是否在启动时同步初始化。 */
    val isAsync: Boolean

    /** 初始化优先级（数字越大越先执行）。 */
    val priority: Int

    /** 执行初始化。 */
    fun init()
}
