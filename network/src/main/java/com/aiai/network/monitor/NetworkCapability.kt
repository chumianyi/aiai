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
package com.aiai.network.monitor

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * 网络能力检测工具。
 *
 * 检测网络的各种能力属性。
 */
object NetworkCapability {

    /**
     * 检查网络是否有流量。
     *
     * @param capabilities 网络能力
     * @return true如果有网络能力
     */
    fun hasInternet(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    /**
     * 检查网络是否为VPN。
     *
     * @param capabilities 网络能力
     * @return true如果是VPN
     */
    fun isVpn(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
    }

    /**
     * 检查网络是否为 Metered（计量收费）。
     *
     * @param capabilities 网络能力
     * @return true如果是计量网络
     */
    fun isMetered(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) != true
    }

    /**
     * 检查网络是否支持HTTPS。
     *
     * @param capabilities 网络能力
     * @return true如果支持
     */
    fun supportsHttps(capabilities: NetworkCapabilities?): Boolean {
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_SUPPORTS_HTTP2) == true
    }

    /**
     * 获取网络带宽。
     *
     * @param capabilities 网络能力
     * @return 带宽（Mbps）
     */
    fun getBandwidth(capabilities: NetworkCapabilities?): Int {
        return capabilities?.linkDownstreamBandwidthKbps?.div(1024) ?: 0
    }

    /**
     * 检查网络是否足够进行大文件传输。
     *
     * @param capabilities 网络能力
     * @return true如果网络质量好
     */
    fun isGoodForLargeTransfer(capabilities: NetworkCapabilities?): Boolean {
        if (capabilities == null) return false
        val bandwidth = getBandwidth(capabilities)
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
            bandwidth >= 10
    }
}
