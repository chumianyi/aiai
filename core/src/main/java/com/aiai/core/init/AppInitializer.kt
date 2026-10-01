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
import com.aiai.common.util.other.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 启动初始化管理器。
 *
 * 管理所有 Initializer，按优先级执行。
 * 支持同步/异步/延迟初始化。
 */
object AppInitializer {

    private val initializers = mutableListOf<Initializer>()

    /** 注册初始化器。 */
    fun register(initializer: Initializer) {
        initializers.add(initializer)
    }

    /** 执行所有初始化。 */
    fun init(context: Context) {
        val sorted = initializers.sortedByDescending { it.priority }
        val asyncList = mutableListOf<Initializer>()

        // 先执行主线程同步初始化
        sorted.filter { it.isMainThread && !it.isAsync }.forEach { initializer ->
            runCatching {
                Logger.d("Initializing: ${initializer.name}")
                initializer.init(context)
            }.onFailure { Logger.e("Init failed: ${initializer.name}", it) }
        }

        // 异步初始化
        sorted.filter { it.isAsync }.forEach { asyncList.add(it) }

        if (asyncList.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                asyncList.forEach { initializer ->
                    runCatching { initializer.init(context) }
                        .onFailure { Logger.e("Async init failed: ${initializer.name}", it) }
                }
            }
        }
    }
}
