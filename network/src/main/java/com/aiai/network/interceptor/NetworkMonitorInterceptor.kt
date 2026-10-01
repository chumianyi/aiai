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

import com.aiai.network.error.ErrorCode
import com.aiai.network.error.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 网络状态检测拦截器，在请求前检测网络是否可用。
 *
 * 功能：
 * - 请求前检测网络连接状态
 * - 无网络时直接抛出ApiException(NETWORK_UNAVAILABLE)
 * - 避免无意义的网络请求
 * - 记录网络状态变化
 */
class NetworkMonitorInterceptor : Interceptor {

    companion object {
        private const val TAG = "NetworkMonitor"
    }

    @Volatile
    private var isNetworkAvailable: Boolean = true

    @Volatile
    private var networkType: String = "unknown"

    /**
     * 设置网络状态。
     *
     * @param available 网络是否可用
     * @param type 网络类型
     */
    fun setNetworkState(available: Boolean, type: String) {
        isNetworkAvailable = available
        networkType = type
    }

    /**
     * 拦截请求，检测网络状态。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络不可用时抛出异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable) {
            throw ApiException(
                code = ErrorCode.NETWORK_UNAVAILABLE,
                message = "网络不可用，请检查网络连接",
                type = "network_error"
            )
        }
        return chain.proceed(chain.request())
    }

    /**
     * 获取当前网络类型。
     *
     * @return 网络类型字符串
     */
    fun getNetworkType(): String = networkType
}
