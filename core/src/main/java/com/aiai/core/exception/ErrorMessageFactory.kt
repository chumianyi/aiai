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

import com.aiai.core.ErrorCode

/**
 * 错误信息工厂。
 *
 * 根据错误码生成用户友好的提示文案。
 */
object ErrorMessageFactory {

    /** 根据错误码获取提示文案。 */
    fun getMessage(code: Int): String {
        return when (code) {
            ErrorCode.SUCCESS -> "成功"
            ErrorCode.UNKNOWN -> "未知错误"
            ErrorCode.PARSE_ERROR -> "数据解析错误"
            ErrorCode.CANCELLED -> "操作已取消"
            ErrorCode.NETWORK_ERROR -> "网络连接失败"
            ErrorCode.NETWORK_TIMEOUT -> "网络请求超时"
            ErrorCode.NETWORK_UNAVAILABLE -> "网络不可用"
            ErrorCode.UNAUTHORIZED -> "请先登录"
            ErrorCode.TOKEN_EXPIRED -> "登录已过期，请重新登录"
            ErrorCode.PERMISSION_DENIED -> "没有操作权限"
            ErrorCode.PARAM_ERROR -> "参数错误"
            ErrorCode.SERVER_ERROR -> "服务器开小差了"
            ErrorCode.SERVER_BUSY -> "服务器繁忙，请稍后重试"
            ErrorCode.NOT_FOUND -> "请求的内容不存在"
            ErrorCode.FILE_NOT_FOUND -> "文件不存在"
            ErrorCode.FILE_TOO_LARGE -> "文件过大"
            ErrorCode.CHAT_SEND_FAILED -> "消息发送失败"
            ErrorCode.CHAT_RATE_LIMITED -> "发送太频繁，请稍后再试"
            ErrorCode.AI_RATE_LIMIT -> "请求太频繁，请稍后再试"
            ErrorCode.AI_CONTENT_FILTERED -> "内容被安全过滤"
            ErrorCode.AI_SERVER_ERROR -> "AI 服务暂时不可用"
            else -> "错误码: $code"
        }
    }
}
