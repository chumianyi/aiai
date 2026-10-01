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
package com.aiai.common.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException

/**
 * 网络相关扩展函数集合。
 *
 * 提供网络类型判断、IP 获取、ping 测试、端口检测等能力。
 */

// region 网络状态

/** 判断网络是否已连接。 */
@Suppress("DEPRECATION")
fun Context.isNetworkAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val info: NetworkInfo? = cm.activeNetworkInfo
        info?.isConnected == true
    }
}

/** 判断是否为 WiFi。 */
@Suppress("DEPRECATION")
fun Context.isWifi(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }
}

/** 判断是否为移动数据。 */
@Suppress("DEPRECATION")
fun Context.isMobileData(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    } else {
        cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE
    }
}

/** 判断是否为以太网。 */
fun Context.isEthernet(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
    val network = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(network) ?: return false
    caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

// endregion

// region IP & Ping

/** 获取本机局域网 IP 地址。 */
fun getLocalIpAddress(): String? {
    return try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (intf in interfaces) {
            val addrs = intf.inetAddresses
            for (addr in addrs) {
                if (!addr.isLoopbackAddress && addr is java.net.Inet4Address) {
                    return addr.hostAddress
                }
            }
        }
        null
    } catch (e: SocketException) {
        null
    }
}

/** Ping 主机 [host]，超时 [timeout] 毫秒，返回是否可达。 */
fun ping(host: String, timeout: Int = 3000): Boolean {
    return try {
        InetAddress.getByName(host).isReachable(timeout)
    } catch (e: IOException) {
        false
    }
}

/** 检测主机 [host] 的 [port] 端口是否开放。 */
fun isPortOpen(host: String, port: Int, timeout: Int = 3000): Boolean {
    return try {
        Socket(host, port).use { it.isConnected }
    } catch (e: IOException) {
        false
    }
}

// endregion
