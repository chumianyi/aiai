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
 * 重试策略接口。
 *
 * 决定失败后是否继续重试。
 */
interface RetryPolicy {
    /** 最大重试次数。 */
    val maxRetries: Int

    /** 是否应该重试。 */
    fun shouldRetry(exception: Throwable, attempt: Int): Boolean

    /** 重试延迟（毫秒）。 */
    fun retryDelayMillis(attempt: Int): Long
}
