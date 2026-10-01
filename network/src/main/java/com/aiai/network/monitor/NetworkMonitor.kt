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

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 网络状态实时监听器。
 *
 * 基于ConnectivityManager监听网络变化，通过Flow发射状态更新。
 *
 * @property context 上下文
 */
class NetworkMonitor(private val context: Context) {

    private val _networkState = MutableStateFlow(NetworkState.UNKNOWN)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateNetworkState()
        }

        override fun onLost(network: Network) {
            _networkState.value = NetworkState.NONE
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            updateNetworkState(capabilities)
        }
    }

    /**
     * 开始监听网络状态。
     */
    fun start() {
        updateNetworkState()
        try {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } catch (e: Exception) {
            // 忽略注册异常
        }
    }

    /**
     * 停止监听网络状态。
     */
    fun stop() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            // 忽略注销异常
        }
    }

    /**
     * 更新网络状态。
     *
     * @param capabilities 网络能力（可选）
     */
    private fun updateNetworkState(capabilities: NetworkCapabilities? = null) {
        val caps = capabilities ?: connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        )

        _networkState.value = when {
            caps == null -> NetworkState.NONE
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.CELLULAR
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkState.ETHERNET
            else -> NetworkState.UNKNOWN
        }
    }

    /**
     * 当前是否有网络连接。
     *
     * @return true如果已连接
     */
    fun isConnected(): Boolean = _networkState.value.isConnected()

    /**
     * 当前是否为WiFi。
     *
     * @return true如果WiFi
     */
    fun isWifi(): Boolean = _networkState.value == NetworkState.WIFI

    /**
     * 当前是否为移动网络。
     *
     * @return true如果CELLULAR
     */
    fun isCellular(): Boolean = _networkState.value == NetworkState.CELLULAR
}
