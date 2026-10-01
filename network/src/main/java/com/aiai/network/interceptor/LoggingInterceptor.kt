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

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * 请求/响应日志拦截器，格式化输出网络请求的详细信息。
 *
 * 输出内容包括：
 * - 请求URL、Method、Protocol
 * - 请求Headers
 * - 请求Body（JSON格式化）
 * - 响应状态码、消息
 * - 响应Headers
 * - 响应Body（JSON格式化，限制最大长度）
 * - 请求耗时
 *
 * @property enabled 是否启用日志，默认false，仅在调试模式下启用
 */
class LoggingInterceptor(
    private val enabled: Boolean = false,
) : Interceptor {

    companion object {
        private const val TAG = "AiAi-Network"
        private const val MAX_LOG_BODY_SIZE = 4096
        private const val DEFAULT_CHARSET = "UTF-8"
    }

    /**
     * 拦截请求和响应，记录详细日志。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (!enabled) {
            return chain.proceed(request)
        }

        // ===== 请求日志 =====
        val requestStart = System.nanoTime()
        val requestLine = "--> ${request.method} ${request.url} ${request.protocol}"
        Log.d(TAG, requestLine)

        // 打印请求Headers
        val headers = request.headers
        if (headers.size > 0) {
            for (i in 0 until headers.size) {
                Log.d(TAG, "    ${headers.name(i)}: ${headers.value(i)}")
            }
        }

        // 打印请求Body
        val requestBody = request.body
        if (requestBody != null) {
            val contentType = requestBody.contentType()
            val bodySize = requestBody.contentLength()
            Log.d(TAG, "    Body Content-Type: $contentType")
            Log.d(TAG, "    Body Content-Length: $bodySize bytes")

            val bufferedSink = Buffer()
            requestBody.writeTo(bufferedSink)
            val charset = contentType?.charset(Charset.forName(DEFAULT_CHARSET))
                ?: Charset.forName(DEFAULT_CHARSET)
            val bodyString = bufferedSink.readString(charset)
            if (bodyString.isNotBlank()) {
                val truncated = if (bodyString.length > MAX_LOG_BODY_SIZE) {
                    bodyString.substring(0, MAX_LOG_BODY_SIZE) + "...(truncated)"
                } else {
                    bodyString
                }
                Log.d(TAG, "    Body: $truncated")
            }
        } else {
            Log.d(TAG, "    Body: <empty>")
        }

        Log.d(TAG, "--> END ${request.method}")

        // ===== 执行请求 =====
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "--> HTTP FAILED: ${e.message}", e)
            throw e
        }

        // ===== 响应日志 =====
        val responseMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - requestStart)
        val responseLine = "<-- ${response.code} ${response.message} ${response.url} (${responseMs}ms)"
        Log.d(TAG, responseLine)

        // 打印响应Headers
        val responseHeaders = response.headers
        if (responseHeaders.size > 0) {
            for (i in 0 until responseHeaders.size) {
                Log.d(TAG, "    ${responseHeaders.name(i)}: ${responseHeaders.value(i)}")
            }
        }

        // 打印响应Body
        val responseBody = response.body
        val contentType = responseBody?.contentType()
        val charset = contentType?.charset(Charset.forName(DEFAULT_CHARSET))
            ?: Charset.forName(DEFAULT_CHARSET)

        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer
        val responseBodyString = buffer?.clone()?.readString(charset) ?: ""

        if (responseBodyString.isNotBlank()) {
            val truncated = if (responseBodyString.length > MAX_LOG_BODY_SIZE) {
                responseBodyString.substring(0, MAX_LOG_BODY_SIZE) + "...(truncated)"
            } else {
                responseBodyString
            }
            Log.d(TAG, "    ResponseBody: $truncated")
        }

        Log.d(TAG, "<-- END HTTP")

        return response
    }
}
