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
package com.aiai.network.error

/**
 * 错误码定义。
 *
 * 统一管理所有网络请求中的错误码，包括：
 * - 网络错误（1000-1099）
 * - 认证错误（2000-2099）
 * - 请求错误（3000-3099）
 * - 服务器错误（5000-5099）
 * - 业务错误（6000-6999）
 */
object ErrorCode {

    // ===== 网络错误 (1000-1099) =====
    /** 网络不可用 */
    const val NETWORK_UNAVAILABLE = 1000

    /** 请求超时 */
    const val REQUEST_TIMEOUT = 1001

    /** 连接失败 */
    const val CONNECTION_FAILED = 1002

    /** DNS解析失败 */
    const val DNS_FAILED = 1003

    /** SSL握手失败 */
    const val SSL_HANDSHAKE_FAILED = 1004

    /** 网络中断 */
    const val NETWORK_INTERRUPTED = 1005

    /** 代理错误 */
    const val PROXY_ERROR = 1006

    /** 重定向过多 */
    const val TOO_MANY_REDIRECTS = 1007

    /** 无网络权限 */
    const val NO_NETWORK_PERMISSION = 1008

    /** 网络类型不支持 */
    const val NETWORK_TYPE_NOT_SUPPORTED = 1009

    // ===== 认证错误 (2000-2099) =====
    /** 未授权 */
    const val UNAUTHORIZED = 2000

    /** Token过期 */
    const val TOKEN_EXPIRED = 2001

    /** Token无效 */
    const val TOKEN_INVALID = 2002

    /** Token刷新失败 */
    const val TOKEN_REFRESH_FAILED = 2003

    /** 权限不足 */
    const val FORBIDDEN = 2004

    /** 需要重新登录 */
    const val LOGIN_REQUIRED = 2005

    /** 账号被禁用 */
    const val ACCOUNT_DISABLED = 2006

    /** API密钥无效 */
    const val API_KEY_INVALID = 2007

    /** 签名验证失败 */
    const val SIGNATURE_INVALID = 2008

    // ===== 请求错误 (3000-3099) =====
    /** 请求参数错误 */
    const val BAD_REQUEST = 3000

    /** 参数缺失 */
    const val PARAM_MISSING = 3001

    /** 参数格式错误 */
    const val PARAM_INVALID = 3002

    /** 资源不存在 */
    const val NOT_FOUND = 3003

    /** 方法不允许 */
    const val METHOD_NOT_ALLOWED = 3004

    /** 请求体过大 */
    const val PAYLOAD_TOO_LARGE = 3005

    /** 不支持的媒体类型 */
    const val UNSUPPORTED_MEDIA_TYPE = 3006

    /** 限流 */
    const val RATE_LIMITED = 3007

    /** 请求被拒绝 */
    const val REQUEST_REJECTED = 3008

    /** 资源冲突 */
    const val RESOURCE_CONFLICT = 3009

    // ===== 服务器错误 (5000-5099) =====
    /** 服务器内部错误 */
    const val SERVER_ERROR = 5000

    /** 服务不可用 */
    const val SERVICE_UNAVAILABLE = 5001

    /** 网关超时 */
    const val GATEWAY_TIMEOUT = 5002

    /** 上游服务错误 */
    const val UPSTREAM_ERROR = 5003

    /** 服务维护中 */
    const val SERVICE_MAINTENANCE = 5004

    /** 数据库错误 */
    const val DATABASE_ERROR = 5005

    /** 第三方服务错误 */
    const val THIRD_PARTY_ERROR = 5006

    // ===== 业务错误 (6000-6999) =====
    /** 模型不存在 */
    const val MODEL_NOT_FOUND = 6000

    /** 模型不可用 */
    const val MODEL_UNAVAILABLE = 6001

    /** Token配额不足 */
    const val TOKEN_QUOTA_EXCEEDED = 6002

    /** 内容审核不通过 */
    const val CONTENT_FILTERED = 6003

    /** 对话不存在 */
    const val CONVERSATION_NOT_FOUND = 6004

    /** 消息不存在 */
    const val MESSAGE_NOT_FOUND = 6005

    /** 文件上传失败 */
    const val FILE_UPLOAD_FAILED = 6006

    /** 文件下载失败 */
    const val FILE_DOWNLOAD_FAILED = 6007

    /** 文件格式不支持 */
    const val FILE_FORMAT_UNSUPPORTED = 6008

    /** 插件不存在 */
    const val PLUGIN_NOT_FOUND = 6009

    /** 插件调用失败 */
    const val PLUGIN_INVOKE_FAILED = 6010

    /** 提示词模板不存在 */
    const val PROMPT_NOT_FOUND = 6011

    /** 搜索服务不可用 */
    const val SEARCH_UNAVAILABLE = 6012

    /** 图片生成失败 */
    const val IMAGE_GENERATION_FAILED = 6013

    /** 语音识别失败 */
    const val SPEECH_RECOGNITION_FAILED = 6014

    /** 语音合成失败 */
    const val SPEECH_SYNTHESIS_FAILED = 6015

    /** 配额已用完 */
    const val QUOTA_EXHAUSTED = 6016

    /** 功能未授权 */
    const val FEATURE_NOT_AUTHORIZED = 6017

    /** 数据同步失败 */
    const val SYNC_FAILED = 6018

    /** 缓存错误 */
    const val CACHE_ERROR = 6019

    /** 未知错误 */
    const val UNKNOWN_ERROR = 9999

    /**
     * 获取错误码对应的默认用户提示信息。
     *
     * @param code 错误码
     * @return 用户友好的错误提示
     */
    fun getDefaultMessage(code: Int): String {
        return when (code) {
            NETWORK_UNAVAILABLE -> "网络不可用，请检查网络连接"
            REQUEST_TIMEOUT -> "请求超时，请稍后重试"
            CONNECTION_FAILED -> "连接失败，请检查网络"
            DNS_FAILED -> "域名解析失败"
            SSL_HANDSHAKE_FAILED -> "安全连接失败"
            UNAUTHORIZED -> "登录已过期，请重新登录"
            TOKEN_EXPIRED -> "登录已过期，请重新登录"
            TOKEN_INVALID -> "登录状态无效"
            FORBIDDEN -> "没有权限执行此操作"
            BAD_REQUEST -> "请求参数错误"
            NOT_FOUND -> "请求的资源不存在"
            RATE_LIMITED -> "请求过于频繁，请稍后再试"
            SERVER_ERROR -> "服务器开小差了，请稍后重试"
            SERVICE_UNAVAILABLE -> "服务暂时不可用"
            TOKEN_QUOTA_EXCEEDED -> "Token配额已用完"
            CONTENT_FILTERED -> "内容包含敏感信息"
            else -> "未知错误，请稍后重试"
        }
    }
}
