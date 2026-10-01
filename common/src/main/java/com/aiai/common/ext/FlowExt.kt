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

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.cancellation.CancellationException

/**
 * Flow 相关扩展函数集合。
 *
 * 提供节流、防抖、重试、超时、错误恢复等能力。
 */

/** 防抖：[timeoutMillis] 内只取最后一个值。 */
fun <T> Flow<T>.debounceTime(timeoutMillis: Long): Flow<T> = debounce(timeoutMillis)

/** 节流：每个 [intervalMillis] 内只取第一个值。 */
fun <T> Flow<T>.throttle(intervalMillis: Long): Flow<T> = flow {
    var lastEmit = 0L
    collect { value ->
        val now = System.currentTimeMillis()
        if (now - lastEmit >= intervalMillis) {
            lastEmit = now
            emit(value)
        }
    }
}

/** 带重试的 Flow，最多 [maxRetries] 次，指数退避。 */
fun <T> Flow<T>.retryExponential(
    maxRetries: Long = 3,
    initialDelay: Long = 1000L
): Flow<T> = retryWhen { cause, attempt ->
    if (cause is CancellationException) return@retryWhen false
    if (attempt < maxRetries) {
        delay(initialDelay * (1 shl attempt.toInt()))
        true
    } else false
}

/** 超时：超过 [timeMillis] 未发射则抛出异常。 */
fun <T> Flow<T>.timeoutMillis(timeMillis: Long): Flow<T> = timeout(timeMillis)

/** 错误恢复：捕获异常并返回 [fallback]。 */
fun <T> Flow<T>.onErrorReturn(fallback: T): Flow<T> = catch { emit(fallback) }

/** 错误恢复：通过 [onError] lambda 返回替代值。 */
fun <T> Flow<T>.onErrorReturnItem(fallback: (Throwable) -> T): Flow<T> = catch { emit(fallback(it)) }

/** 延迟发射每个元素 [delayMillis]。 */
fun <T> Flow<T>.delayEach(delayMillis: Long): Flow<T> = onEach { delay(delayMillis) }

/** 转换并发射。 */
fun <T, R> Flow<T>.mapNotNull(transform: (T) -> R?): Flow<R> = transform { value ->
    transform(value)?.let { emit(it) }
}

/** 过滤重复连续值（与 distinctUntilChanged 等价，这里用自定义实现）。 */
fun <T> Flow<T>.distinctUntilChangedFlow(): Flow<T> {
    var last: Any? = Any()
    return transform { value ->
        if (value != last) {
            last = value
            emit(value)
        }
    }
}

/** 安全收集，捕获取消异常。 */
suspend fun <T> Flow<T>.collectSafe(action: suspend (T) -> Unit) {
    try {
        collect { action(it) }
    } catch (e: CancellationException) {
        // 正常取消，忽略
    }
}
