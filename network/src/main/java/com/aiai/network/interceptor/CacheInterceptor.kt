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
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 缓存拦截器，实现离线缓存和缓存有效期策略。
 *
 * 缓存策略：
 * - 有网络时：使用网络响应，同时更新缓存
 * - 无网络时：使用缓存响应
 * - 缓存有效期由cacheMaxAgeSeconds控制
 * - 缓存过期后返回504状态码，触发错误处理
 *
 * @property config API配置
 */
class CacheInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val HEADER_CACHE_CONTROL = "Cache-Control"
        private const val HEADER_PRAGMA = "Pragma"
        private const val CACHE_CONTROL_PUBLIC = "public"
        private const val CACHE_CONTROL_MAX_AGE = "max-age="
        private const val CACHE_CONTROL_ONLY_IF_CACHED = "only-if-cached"
        private const val CACHE_CONTROL_NO_CACHE = "no-cache"
    }

    @Volatile
    private var isNetworkAvailable: Boolean = true

    /**
     * 设置网络状态。
     *
     * @param available 网络是否可用
     */
    fun setNetworkAvailable(available: Boolean) {
        isNetworkAvailable = available
    }

    /**
     * 拦截请求，根据网络状态和缓存策略处理请求。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!config.enableCache) {
            return chain.proceed(request)
        }

        request = if (isNetworkAvailable) {
            // 有网络：使用网络响应，缓存有效期为cacheMaxAgeSeconds
            request.newBuilder()
                .header(
                    HEADER_CACHE_CONTROL,
                    "$CACHE_CONTROL_PUBLIC, $CACHE_CONTROL_MAX_AGE${config.cacheMaxAgeSeconds}"
                )
                .build()
        } else {
            // 无网络：仅使用缓存
            request.newBuilder()
                .header(HEADER_CACHE_CONTROL, CACHE_CONTROL_ONLY_IF_CACHED)
                .build()
        }

        val response = chain.proceed(request)

        // 有网络时，添加缓存控制头
        return if (isNetworkAvailable) {
            response.newBuilder()
                .header(
                    HEADER_CACHE_CONTROL,
                    "$CACHE_CONTROL_PUBLIC, $CACHE_CONTROL_MAX_AGE${config.cacheMaxAgeSeconds}"
                )
                .removeHeader(HEADER_PRAGMA)
                .build()
        } else {
            response
        }
    }
}
