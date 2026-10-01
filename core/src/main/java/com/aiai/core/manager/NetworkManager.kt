/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.core.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 网络状态管理器。
 *
 * 提供网络状态监听、网络类型判断、网络可用性检查等功能。
 */
object NetworkManager {

    private const val TAG = "NetworkManager"

    /**
     * 网络类型枚举。
     */
    enum class NetworkType {
        /** 无网络 */
        NONE,
        /** WiFi */
        WIFI,
        /** 移动数据 */
        CELLULAR,
        /** 以太网 */
        ETHERNET,
        /** 其他 */
        OTHER
    }

    /**
     * 网络状态数据类。
     *
     * @property isAvailable 网络是否可用
     * @property type 网络类型
     * @property isMetered 是否计费
     */
    data class NetworkState(
        val isAvailable: Boolean = false,
        val type: NetworkType = NetworkType.NONE,
        val isMetered: Boolean = false
    )

    private val _networkState = MutableStateFlow(NetworkState())
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private var connectivityManager: ConnectivityManager? = null
    private var callback: ConnectivityManager.NetworkCallback? = null

    /**
     * 初始化。
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        registerNetworkCallback()
        Log.d(TAG, "NetworkManager initialized")
    }

    /**
     * 注册网络回调。
     */
    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateNetworkState(network)
            }

            override fun onLost(network: Network) {
                _networkState.value = NetworkState(isAvailable = false)
                Log.d(TAG, "Network lost")
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                updateNetworkState(network, capabilities)
            }
        }

        connectivityManager?.registerNetworkCallback(request, callback!!)
    }

    /**
     * 更新网络状态。
     */
    private fun updateNetworkState(network: Network, capabilities: NetworkCapabilities? = null) {
        val caps = capabilities ?: connectivityManager?.getNetworkCapabilities(network)
        val type = when {
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkType.WIFI
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkType.CELLULAR
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> NetworkType.ETHERNET
            else -> NetworkType.OTHER
        }
        val isMetered = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) != true

        _networkState.value = NetworkState(
            isAvailable = true,
            type = type,
            isMetered = isMetered
        )
        Log.d(TAG, "Network available: $type, metered=$isMetered")
    }

    /**
     * 检查网络是否可用。
     *
     * @return 网络是否可用
     */
    fun isNetworkAvailable(): Boolean {
        return _networkState.value.isAvailable
    }

    /**
     * 获取当前网络类型。
     *
     * @return 网络类型
     */
    fun getNetworkType(): NetworkType {
        return _networkState.value.type
    }

    /**
     * 是否是 WiFi。
     *
     * @return 是否是 WiFi
     */
    fun isWifi(): Boolean {
        return _networkState.value.type == NetworkType.WIFI
    }

    /**
     * 是否是移动数据。
     *
     * @return 是否是移动数据
     */
    fun isCellular(): Boolean {
        return _networkState.value.type == NetworkType.CELLULAR
    }

    /**
     * 是否计费网络。
     *
     * @return 是否计费
     */
    fun isMetered(): Boolean {
        return _networkState.value.isMetered
    }

    /**
     * 释放资源。
     */
    fun release() {
        callback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
        }
        callback = null
        connectivityManager = null
        Log.d(TAG, "NetworkManager released")
    }
}
