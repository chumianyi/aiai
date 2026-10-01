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
import java.util.Locale

/**
 * 公共Header拦截器，为每个请求添加公共请求头。
 *
 * 添加的Header包括：
 * - User-Agent: 应用版本、设备信息
 * - Accept-Language: 设备语言
 * - X-Device-Id: 设备唯一标识
 * - X-App-Version: 应用版本号
 * - X-Platform: 平台标识（android）
 * - X-Network-Type: 网络类型
 *
 * @property config API配置
 */
class HeaderInterceptor(
    private val config: ApiConfig = ApiConfig.getInstance(),
) : Interceptor {

    companion object {
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
        private const val HEADER_DEVICE_ID = "X-Device-Id"
        private const val HEADER_APP_VERSION = "X-App-Version"
        private const val HEADER_PLATFORM = "X-Platform"
        private const val HEADER_NETWORK_TYPE = "X-Network-Type"
        private const val PLATFORM_ANDROID = "android"
    }

    @Volatile
    private var networkType: String = "wifi"

    /**
     * 设置网络类型。
     *
     * @param type 网络类型（wifi/cellular/ethernet/none）
     */
    fun setNetworkType(type: String) {
        networkType = type
    }

    /**
     * 拦截请求，添加公共Header。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header(HEADER_USER_AGENT, config.buildUserAgent())
            .header(HEADER_ACCEPT_LANGUAGE, Locale.getDefault().toLanguageTag())
            .header(HEADER_DEVICE_ID, config.deviceId)
            .header(HEADER_APP_VERSION, config.appVersion)
            .header(HEADER_PLATFORM, PLATFORM_ANDROID)
            .header(HEADER_NETWORK_TYPE, networkType)
            .build()

        return chain.proceed(newRequest)
    }
}
