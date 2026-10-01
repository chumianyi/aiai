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
 * 通用API响应包装。
 *
 * 所有API响应都使用此包装格式。
 *
 * @property code 业务状态码，0表示成功
 * @property message 提示信息
 * @property data 响应数据
 * @property success 是否成功
 */
data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int = 0,

    @SerializedName("message")
    val message: String = "success",

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("success")
    val success: Boolean = code == 0,
) {
    /**
     * 判断响应是否成功。
     *
     * @return true如果code为0且data非空
     */
    fun isSuccessful(): Boolean = code == 0 && data != null

    /**
     * 获取数据，如果失败则抛出异常。
     *
     * @return 数据
     * @throws ApiException 当响应失败时
     */
    fun getDataOrThrow(): T {
        if (!isSuccessful()) {
            throw com.aiai.network.error.ApiException(
                code = code,
                message = message,
                type = "api_error"
            )
        }
        return data!!
    }

    companion object {
        /**
         * 创建成功响应。
         *
         * @param data 响应数据
         * @return 成功的ApiResponse
         */
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(
            code = 0,
            message = "success",
            data = data,
            success = true,
        )

        /**
         * 创建错误响应。
         *
         * @param code 错误码
         * @param message 错误信息
         * @return 错误的ApiResponse
         */
        fun <T> error(code: Int, message: String): ApiResponse<T> = ApiResponse(
            code = code,
            message = message,
            data = null,
            success = false,
        )
    }
}
