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
package com.aiai.core.init

import android.content.Context

/**
 * 启动初始化器接口。
 *
 * 每个需要在启动时初始化的组件实现此接口。
 */
interface Initializer {
    /** 组件名。 */
    val name: String

    /** 是否在主线程同步初始化。 */
    val isMainThread: Boolean get() = true

    /** 是否异步初始化（不阻塞启动）。 */
    val isAsync: Boolean get() = false

    /** 初始化优先级（数字越大越先执行）。 */
    val priority: Int get() = 0

    /** 执行初始化。 */
    fun init(context: Context)
}
