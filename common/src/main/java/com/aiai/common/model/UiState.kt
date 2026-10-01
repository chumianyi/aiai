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
 * UI 状态密封类。
 */
sealed class UiState<out T> {
    /** 空闲（初始状态）。 */
    object Idle : UiState<Nothing>()

    /** 加载中。 */
    object Loading : UiState<Nothing>()

    /** 成功。 */
    data class Success<out T>(val data: T) : UiState<T>()

    /** 错误。 */
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()

    /** 空数据。 */
    object Empty : UiState<Nothing>()
}
