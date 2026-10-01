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
package com.aiai.core.exception

/**
 * 错误消息工厂。
 */
object ErrorMessageFactory {

    fun getMessage(code: Int): String {
        return when (code) {
            ErrorCode.SUCCESS -> "成功"
            ErrorCode.UNKNOWN_ERROR -> "未知错误"
            ErrorCode.NETWORK_NO_CONNECTION -> "网络未连接，请检查网络设置"
            ErrorCode.NETWORK_TIMEOUT -> "网络请求超时，请稍后重试"
            ErrorCode.NETWORK_CONNECTION_ERROR -> "网络连接失败"
            ErrorCode.NETWORK_SSL_ERROR -> "安全连接失败"
            ErrorCode.NETWORK_IO_ERROR -> "网络读写错误"
            ErrorCode.NETWORK_PARSE_ERROR -> "数据解析错误"
            ErrorCode.HTTP_BAD_REQUEST -> "请求参数错误"
            ErrorCode.HTTP_UNAUTHORIZED -> "未登录或登录已过期"
            ErrorCode.HTTP_FORBIDDEN -> "无权限访问"
            ErrorCode.HTTP_NOT_FOUND -> "请求的资源不存在"
            ErrorCode.HTTP_SERVER_ERROR -> "服务器内部错误"
            ErrorCode.BUSINESS_ERROR -> "业务处理失败"
            ErrorCode.BUSINESS_PARAM_ERROR -> "参数错误"
            ErrorCode.AUTH_NOT_LOGIN -> "请先登录"
            ErrorCode.AUTH_TOKEN_EXPIRED -> "登录已过期，请重新登录"
            ErrorCode.AUTH_TOKEN_INVALID -> "登录状态无效"
            ErrorCode.PERMISSION_DENIED -> "权限被拒绝"
            else -> "操作失败，请稍后重试"
        }
    }
}
