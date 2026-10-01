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
package com.aiai.network.interceptor

import com.aiai.network.config.ApiConfig
import com.aiai.network.error.ErrorCode
import com.aiai.network.error.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * 重试拦截器，对网络错误进行自动重试。
 *
 * 支持：
 * - 指数退避策略（第1次1s，第2次2s，第3次4s...）
 * - 可配置最大重试次数
 * - 仅对网络错误（连接失败、超时、IO异常）重试
 * - 不对业务错误（4xx、5xx响应）重试，由ErrorHandler处理
 *
 * @property config API配置
 */
class RetryInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val TAG = "RetryInterceptor"
        private const val MAX_BACKOFF_DELAY = 30_000L
    }

    /**
     * 拦截请求，在网络错误时进行重试。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 重试耗尽后抛出最后一次异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastException: IOException? = null

        while (attempt <= config.retryMaxAttempts) {
            try {
                val response = chain.proceed(request)
                // 网络请求成功，直接返回
                return response
            } catch (e: IOException) {
                lastException = e
                attempt++

                // 达到最大重试次数，抛出异常
                if (attempt > config.retryMaxAttempts) {
                    break
                }

                // 判断是否为可重试的异常
                if (!isRetryable(e)) {
                    throw e
                }

                // 计算退避延迟
                val delayMs = config.calculateBackoffDelay(attempt)
                try {
                    Thread.sleep(delayMs)
                } catch (interruptedException: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw IOException("Retry interrupted", interruptedException)
                }
            }
        }

        // 重试耗尽，抛出最后一次异常
        throw lastException ?: IOException("Unknown retry failure")
    }

    /**
     * 判断异常是否可重试。
     *
     * 可重试的异常类型：
     * - ConnectException: 连接失败
     * - SocketTimeoutException: 超时
     * - UnknownHostException: DNS解析失败（可能临时）
     * - 其他IOException: 网络中断
     *
     * @param e 异常
     * @return true如果可重试
     */
    private fun isRetryable(e: IOException): Boolean {
        return when (e) {
            is ConnectException -> true
            is SocketTimeoutException -> true
            is UnknownHostException -> true
            else -> {
                val message = e.message ?: ""
                message.contains("timeout", ignoreCase = true) ||
                    message.contains("connect", ignoreCase = true) ||
                    message.contains("network", ignoreCase = true)
            }
        }
    }
}
