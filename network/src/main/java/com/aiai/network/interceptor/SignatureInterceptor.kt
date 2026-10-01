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
import com.aiai.network.sign.HmacSigner
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 请求签名拦截器，为每个请求添加签名头。
 *
 * 签名算法：
 * 1. 生成时间戳（当前Unix时间，秒）
 * 2. 生成随机nonce字符串
 * 3. 拼接签名字符串：timestamp + nonce + method + path + body
 * 4. 使用HMAC-SHA256计算签名
 * 5. 添加X-Timestamp、X-Nonce、X-Signature请求头
 *
 * @property config API配置
 */
class SignatureInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val HEADER_TIMESTAMP = "X-Timestamp"
        private const val HEADER_NONCE = "X-Nonce"
        private const val HEADER_SIGNATURE = "X-Signature"
        private const val HEADER_APP_ID = "X-App-Id"
        private const val APP_ID = "aiai_android"
    }

    private val signer = HmacSigner()
    private val nonceGenerator = java.util.UUID.randomUUID().toString()

    /**
     * 拦截请求，添加签名头。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (!config.isSignNeeded()) {
            return chain.proceed(originalRequest)
        }

        // 读取请求Body
        val bodyBuffer = Buffer()
        originalRequest.body?.writeTo(bodyBuffer)
        val bodyString = bodyBuffer.readUtf8()

        // 生成签名参数
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())
        val nonce = nonceGenerator

        // 构建签名字符串
        val path = originalRequest.url.encodedPath
        val method = originalRequest.method
        val signSource = "$timestamp$nonce$method$path$bodyString"

        // 计算签名
        val signature = signer.sign(signSource, config.signSecret)

        // 构建新请求，添加签名头
        val newRequest = originalRequest.newBuilder()
            .header(HEADER_TIMESTAMP, timestamp)
            .header(HEADER_NONCE, nonce)
            .header(HEADER_SIGNATURE, signature)
            .header(HEADER_APP_ID, APP_ID)
            .build()

        return chain.proceed(newRequest)
    }
}
