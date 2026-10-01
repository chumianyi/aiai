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
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

/**
 * 加密拦截器，对请求体进行AES加密，对响应体进行解密。
 *
 * 加密算法：
 * - 请求体：AES/CBC/PKCS5Padding加密后Base64编码
 * - 响应体：Base64解码后AES/CBC/PKCS5Padding解密
 *
 * @property config API配置
 */
class EncryptionInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val ALGORITHM_AES = "AES"
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val HEADER_CONTENT_ENCRYPTED = "X-Content-Encrypted"
        private const val HEADER_VALUE_ENCRYPTED = "1"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_JSON = "application/json; charset=utf-8"
    }

    private val secureRandom = SecureRandom()

    /**
     * 拦截请求，对请求体加密，对响应体解密。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (!config.isEncryptionNeeded()) {
            return chain.proceed(originalRequest)
        }

        // 加密请求体
        val encryptedRequest = encryptRequestBody(originalRequest)
        // 执行请求
        val response = chain.proceed(encryptedRequest)
        // 解密响应体
        return decryptResponseBody(response)
    }

    /**
     * 加密请求体。
     *
     * @param request 原始请求
     * @return 加密后的请求
     */
    private fun encryptRequestBody(request: okhttp3.Request): okhttp3.Request {
        val requestBody = request.body ?: return request

        // 读取原始Body
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        val originalBody = buffer.readUtf8()

        if (originalBody.isBlank()) return request

        // AES加密
        val encryptedBody = try {
            val secretKey = SecretKeySpec(config.encryptionKey.toByteArray(), ALGORITHM_AES)
            val iv = IvParameterSpec(config.encryptionIv.toByteArray())
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
            val encrypted = cipher.doFinal(originalBody.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            return request
        }

        // 构建新请求Body
        val newBody = RequestBody.create(
            okhttp3.MediaType.parse(CONTENT_TYPE_JSON),
            encryptedBody
        )

        return request.newBuilder()
            .method(request.method, newBody)
            .header(HEADER_CONTENT_ENCRYPTED, HEADER_VALUE_ENCRYPTED)
            .build()
    }

    /**
     * 解密响应体。
     *
     * @param response 原始响应
     * @return 解密后的响应
     */
    private fun decryptResponseBody(response: Response): Response {
        val responseBody = response.body ?: return response
        val bodyString = responseBody.string()

        if (bodyString.isBlank()) return response

        // 检查是否为加密响应
        val encryptedHeader = response.header(HEADER_CONTENT_ENCRYPTED)
        if (encryptedHeader != HEADER_VALUE_ENCRYPTED) {
            // 不是加密响应，重新构建body返回
            return response.newBuilder()
                .body(okhttp3.ResponseBody.create(
                    okhttp3.MediaType.parse(CONTENT_TYPE_JSON),
                    bodyString
                ))
                .build()
        }

        // AES解密
        val decryptedBody = try {
            val encryptedBytes = Base64.decode(bodyString, Base64.NO_WRAP)
            val secretKey = SecretKeySpec(config.encryptionKey.toByteArray(), ALGORITHM_AES)
            val iv = IvParameterSpec(config.encryptionIv.toByteArray())
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
            String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
        } catch (e: Exception) {
            bodyString
        }

        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(
                okhttp3.MediaType.parse(CONTENT_TYPE_JSON),
                decryptedBody
            ))
            .build()
    }
}
