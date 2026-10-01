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

/**
 * 启动初始化器接口。
 */
interface Initializer {
    fun init(context: Context)
    fun isAsync(): Boolean = false
    fun isDelay(): Boolean = false
}

/**
 * 启动初始化管理器。
 */
object AppInitializer {
    private val initializers = mutableListOf<Initializer>()

    fun init(context: Context) {
        Logger.d("AppInitializer start")
        initializers.forEach { initializer ->
            try {
                initializer.init(context)
                Logger.d("Initialized: ${initializer.javaClass.simpleName}")
            } catch (e: Exception) {
                Logger.e("AppInitializer", e)
            }
        }
        Logger.d("AppInitializer done, total: ${initializers.size}")
    }

    fun register(initializer: Initializer) {
        initializers.add(initializer)
    }
}
