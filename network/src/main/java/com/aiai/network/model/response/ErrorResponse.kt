/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 错误响应模型。
 *
 * 服务器返回的错误信息结构。
 *
 * @property error 错误详情
 */
data class ErrorResponse(
    @SerializedName("error")
    val error: ErrorDetail = ErrorDetail(),
)

/**
 * 错误详情。
 *
 * @property code 错误码
 * @property message 错误信息
 * @property type 错误类型
 * @property param 出错的参数名
 */
data class ErrorDetail(
    @SerializedName("code")
    val code: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("param")
    val param: String? = null,
)
