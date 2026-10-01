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
package com.aiai.common.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Lifecycle 相关扩展函数集合。
 *
 * 提供 launchWhenStarted / launchWhenResumed / repeatOnLifecycle 等生命周期感知协程启动。
 */

/** 在 ON_START 状态启动协程。 */
fun LifecycleOwner.launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) { block() }
    }
}

/** 在 ON_RESUMED 状态启动协程。 */
fun LifecycleOwner.launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) { block() }
    }
}

/** 在 ON_CREATED 状态启动协程。 */
fun LifecycleOwner.launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) { block() }
    }
}

/** 主线程执行。 */
suspend fun <T> withMain(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Main, block)
}

/** IO 线程执行。 */
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO, block)
}

/** Default 线程执行。 */
suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Default, block)
}

/** 判断当前是否至少处于 STARTED 状态。 */
fun LifecycleOwner.isAtLeastStarted(): Boolean {
    return lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
}

/** 判断当前是否至少处于 RESUMED 状态。 */
fun LifecycleOwner.isAtLeastResumed(): Boolean {
    return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
}

/** 判断当前是否至少处于 CREATED 状态。 */
fun LifecycleOwner.isAtLeastCreated(): Boolean {
    return lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)
}
