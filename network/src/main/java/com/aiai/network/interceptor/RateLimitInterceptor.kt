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

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 限流拦截器，控制单位时间内的请求数量。
 *
 * 采用令牌桶算法：
 * - 每秒生成maxRequests个令牌
 * - 每个请求消耗一个令牌
 * - 令牌不足时阻塞等待
 * - 防止短时间内发送过多请求导致服务器限流
 *
 * @property maxRequestsPerSecond 每秒最大请求数，默认10
 */
class RateLimitInterceptor(
    private val maxRequestsPerSecond: Int = 10,
) : Interceptor {

    companion object {
        private const val TAG = "RateLimitInterceptor"
        private const val MAX_WAIT_MS = 5_000L
    }

    /** 令牌桶，存储可用令牌的时间戳 */
    private val tokenBucket = ConcurrentLinkedQueue<Long>()

    /** 上次补充令牌的时间 */
    private val lastRefillTime = AtomicLong(System.currentTimeMillis())

    /**
     * 拦截请求，进行限流控制。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     * @throws RateLimitExceededException 超过限流等待时间
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        refillTokens()

        val token = tokenBucket.poll()
        if (token == null) {
            // 令牌不足，等待
            val waitStart = System.currentTimeMillis()
            while (tokenBucket.isEmpty()) {
                refillTokens()
                if (tokenBucket.isNotEmpty()) break
                val elapsed = System.currentTimeMillis() - waitStart
                if (elapsed > MAX_WAIT_MS) {
                    throw RateLimitExceededException("Rate limit exceeded, waited ${elapsed}ms")
                }
                Thread.sleep(10)
            }
        }

        return chain.proceed(chain.request())
    }

    /**
     * 补充令牌桶，每秒补充maxRequestsPerSecond个令牌。
     */
    private fun refillTokens() {
        val now = System.currentTimeMillis()
        val lastRefill = lastRefillTime.get()
        val elapsed = now - lastRefill

        if (elapsed >= 1000) {
            val tokensToAdd = (elapsed / 1000.0 * maxRequestsPerSecond).toInt()
            if (tokensToAdd > 0) {
                for (i in 0 until tokensToAdd) {
                    tokenBucket.offer(now)
                }
                lastRefillTime.set(now)
            }
        }
    }

    /**
     * 重置限流状态。
     */
    fun reset() {
        tokenBucket.clear()
        lastRefillTime.set(System.currentTimeMillis())
    }
}

/**
 * 限流超限异常。
 *
 * @param message 异常信息
 */
class RateLimitExceededException(message: String) : IOException(message)
