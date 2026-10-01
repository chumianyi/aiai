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
 * 资源密封类，用于包装网络/数据库请求结果。
 *
 * @param T 成功时的数据类型
 */
sealed class Resource<out T> {
    /** 加载中。 */
    object Loading : Resource<Nothing>()

    /** 成功。 */
    data class Success<out T>(val data: T) : Resource<T>()

    /** 错误。 */
    data class Error(val exception: Throwable, val message: String? = null) : Resource<Nothing>()

    /** 是否为加载中。 */
    fun isLoading() = this is Loading

    /** 是否为成功。 */
    fun isSuccess() = this is Success

    /** 是否为错误。 */
    fun isError() = this is Error

    /** 获取成功数据（可空）。 */
    fun getDataOrNull(): T? = if (this is Success) data else null

    /** 获取错误信息（可空）。 */
    fun getErrorOrNull(): String? = if (this is Error) (message ?: exception.message) else null
}
