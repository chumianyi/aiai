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
 * 可选值包装类。
 *
 * 类似 Java 的 Optional，但更轻量。
 */
sealed class Optional<out T> {
    /** 有值。 */
    data class Some<out T>(val value: T) : Optional<T>()

    /** 空值。 */
    object None : Optional<Nothing>()

    /** 是否有值。 */
    fun isSome(): Boolean = this is Some

    /** 是否为空。 */
    fun isNone(): Boolean = this is None

    /** 获取值或默认值。 */
    fun getOrNull(): T? = if (this is Some) value else null

    /** 获取值或 [default]。 */
    fun getOrDefault(default: @UnsafeVariance T): T {
        return if (this is Some) value else default
    }
}

/** 将可空类型包装为 Optional。 */
fun <T> T?.toOptional(): Optional<T> {
    return if (this == null) Optional.None else Optional.Some(this)
}
