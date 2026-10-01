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
package com.aiai.common.util

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.util.Log

/**
 * WiFi 工具类。
 *
 * 提供开关、扫描、连接、信号强度、速度测试等功能。
 */
object WifiUtil {

    private const val TAG = "WifiUtil"

    /**
     * 获取 WiFi 管理器。
     */
    private fun getWifiManager(context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * 检查 WiFi 是否开启。
     *
     * @param context 上下文
     * @return true 表示已开启
     */
    fun isWifiEnabled(context: Context): Boolean {
        return getWifiManager(context).isWifiEnabled
    }

    /**
     * 开启 WiFi。
     *
     * @param context 上下文
     * @return true 表示成功
     */
    fun enableWifi(context: Context): Boolean {
        val wifiManager = getWifiManager(context)
        return if ((wifiManager.isWifiEnabled as? Boolean) == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android Q+ 无法直接开启，需引导用户到设置页
                false
            } else {
                @Suppress("DEPRECATION")
                wifiManager.isWifiEnabled = true
            }
        } else {
            true
        }
    }

    /**
     * 关闭 WiFi。
     *
     * @param context 上下文
     * @return true 表示成功
     */
    fun disableWifi(context: Context): Boolean {
        val wifiManager = getWifiManager(context)
        return if ((wifiManager.isWifiEnabled as? Boolean) == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                false
            } else {
                @Suppress("DEPRECATION")
                wifiManager.isWifiEnabled = false
            }
        } else {
            true
        }
    }

    /**
     * 开始扫描 WiFi 列表。
     *
     * @param context 上下文
     * @return 扫描结果列表
     */
    fun scanWifi(context: Context): List<ScanResult> {
        return try {
            val wifiManager = getWifiManager(context)
            wifiManager.startScan()
            wifiManager.scanResults
        } catch (e: Exception) {
            Log.e(TAG, "Scan wifi failed", e)
            emptyList()
        }
    }

    /**
     * 获取当前连接的 WiFi 信息。
     *
     * @param context 上下文
     * @return WiFi 信息
     */
    fun getCurrentWifiInfo(context: Context): android.net.wifi.WifiInfo? {
        val wifiManager = getWifiManager(context)
        return wifiManager.connectionInfo
    }

    /**
     * 获取当前 WiFi SSID。
     *
     * @param context 上下文
     * @return SSID
     */
    fun getCurrentSsid(context: Context): String {
        val info = getCurrentWifiInfo(context) ?: return ""
        return info.ssid.replace("\"", "")
    }

    /**
     * 获取信号强度等级。
     *
     * @param context 上下文
     * @param numLevels 等级数
     * @return 信号强度等级（0-numLevels）
     */
    fun getSignalLevel(context: Context, numLevels: Int = 5): Int {
        val info = getCurrentWifiInfo(context) ?: return 0
        return WifiManager.calculateSignalLevel(info.rssi, numLevels)
    }

    /**
     * 获取信号强度百分比。
     *
     * @param context 上下文
     * @return 信号强度百分比（0-100）
     */
    fun getSignalStrengthPercent(context: Context): Int {
        val info = getCurrentWifiInfo(context) ?: return 0
        val rssi = info.rssi
        // RSSI 通常在 -100 到 -50 之间
        return ((rssi + 100).coerceIn(0, 50) * 100 / 50)
    }

    /**
     * 获取 IP 地址。
     *
     * @param context 上下文
     * @return IP 地址字符串
     */
    fun getIpAddress(context: Context): String {
        val info = getCurrentWifiInfo(context) ?: return ""
        val ip = info.ipAddress
        return String.format(
            "%d.%d.%d.%d",
            ip and 0xff,
            ip shr 8 and 0xff,
            ip shr 16 and 0xff,
            ip shr 24 and 0xff
        )
    }

    /**
     * 检查是否连接到 WiFi。
     *
     * @param context 上下文
     * @return true 表示已连接
     */
    fun isConnected(context: Context): Boolean {
        val info = getCurrentWifiInfo(context) ?: return false
        return info.networkId != -1
    }

    /**
     * 获取 WiFi  MAC 地址。
     *
     * @param context 上下文
     * @return MAC 地址
     */
    fun getMacAddress(context: Context): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android 6.0+ 返回假地址
                "02:00:00:00:00:00"
            } else {
                @Suppress("DEPRECATION")
                getCurrentWifiInfo(context)?.macAddress ?: ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 断开当前连接。
     *
     * @param context 上下文
     * @return true 表示成功
     */
    fun disconnect(context: Context): Boolean {
        val wifiManager = getWifiManager(context)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            wifiManager.disconnect()
        } else {
            false
        }
    }

    /**
     * 格式化 WiFi 列表（按信号强度排序）。
     *
     * @param scanResults 扫描结果
     * @return 排序后的列表
     */
    fun sortBySignalStrength(scanResults: List<ScanResult>): List<ScanResult> {
        return scanResults.sortedByDescending { it.level }
    }

    /**
     * 检查是否为 5GHz WiFi。
     *
     * @param frequency 频率
     * @return true 表示是 5GHz
     */
    fun is5GHz(frequency: Int): Boolean {
        return frequency in 4900..5800
    }

    /**
     * 检查是否为 2.4GHz WiFi。
     *
     * @param frequency 频率
     * @return true 表示是 2.4GHz
     */
    fun is24GHz(frequency: Int): Boolean {
        return frequency in 2400..2500
    }

    /**
     * 获取频率对应的频段。
     *
     * @param frequency 频率
     * @return 频段描述
     */
    fun getFrequencyBand(frequency: Int): String {
        return when {
            is5GHz(frequency) -> "5GHz"
            is24GHz(frequency) -> "2.4GHz"
            else -> "未知"
        }
    }
}
