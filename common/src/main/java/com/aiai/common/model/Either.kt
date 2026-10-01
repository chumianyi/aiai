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
package com.aiai.common.model

/**
 * 左右联合类型（Either）。
 *
 * 类似于 Rust 的 Result，或 Haskell 的 Either。
 * 左值通常表示错误，右值表示成功。
 */
sealed class Either<out L, out R> {
    /** 左值（通常表示错误）。 */
    data class Left<out L>(val value: L) : Either<L, Nothing>()

    /** 右值（通常表示成功）。 */
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    /** 是否为左值。 */
    fun isLeft(): Boolean = this is Left

    /** 是否为右值。 */
    fun isRight(): Boolean = this is Right

    /** 获取左值（可空）。 */
    fun leftOrNull(): L? = if (this is Left) value else null

    /** 获取右值（可空）。 */
    fun rightOrNull(): R? = if (this is Right) value else null
}
