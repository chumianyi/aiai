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
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import android.telephony.TelephonyManager
import android.util.Log

/**
 * 电话工具类。
 *
 * 提供设备ID、网络类型、信号强度、SIM信息等功能。
 */
object TelephonyUtil {

    private const val TAG = "TelephonyUtil"

    /**
     * 获取电话管理器。
     */
    private fun getTelephonyManager(context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    /**
     * 获取设备编号（IMEI/MEID）。
     *
     * @param context 上下文
     * @return 设备编号
     */
    @Suppress("MissingPermission")
    fun getDeviceId(context: Context): String {
        return try {
            val telephonyManager = getTelephonyManager(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei ?: ""
            } else {
                @Suppress("DEPRECATION")
                telephonyManager.deviceId ?: ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get device id failed", e)
            ""
        }
    }

    /**
     * 获取 SIM 卡序列号。
     *
     * @param context 上下文
     * @return SIM 序列号
     */
    @Suppress("MissingPermission")
    fun getSimSerialNumber(context: Context): String {
        return try {
            val telephonyManager = getTelephonyManager(context)
            telephonyManager.simSerialNumber ?: ""
        } catch (e: Exception) {
            Log.e(TAG, "Get sim serial failed", e)
            ""
        }
    }

    /**
     * 获取手机号码。
     *
     * @param context 上下文
     * @return 手机号码
     */
    @Suppress("MissingPermission")
    fun getPhoneNumber(context: Context): String {
        return try {
            val telephonyManager = getTelephonyManager(context)
            telephonyManager.line1Number ?: ""
        } catch (e: Exception) {
            Log.e(TAG, "Get phone number failed", e)
            ""
        }
    }

    /**
     * 获取网络运营商名称。
     *
     * @param context 上下文
     * @return 运营商名称
     */
    fun getNetworkOperatorName(context: Context): String {
        return try {
            val telephonyManager = getTelephonyManager(context)
            telephonyManager.networkOperatorName ?: ""
        } catch (e: Exception) {
            Log.e(TAG, "Get operator name failed", e)
            ""
        }
    }

    /**
     * 获取网络类型。
     *
     * @param context 上下文
     * @return 网络类型字符串
     */
    fun getNetworkTypeName(context: Context): String {
        val telephonyManager = getTelephonyManager(context)
        val networkType = telephonyManager.networkType
        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
            TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
            TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
            TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO_0"
            TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO_A"
            TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
            TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
            TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
            TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
            TelephonyManager.NETWORK_TYPE_IDEN -> "IDEN"
            TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO_B"
            TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
            TelephonyManager.NETWORK_TYPE_EHRPD -> "EHRPD"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPAP"
            TelephonyManager.NETWORK_TYPE_GSM -> "GSM"
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "TD_SCDMA"
            TelephonyManager.NETWORK_TYPE_IWLAN -> "IWLAN"
            else -> "未知($networkType)"
        }
    }

    /**
     * 检查是否为 5G 网络。
     *
     * @param context 上下文
     * @return true 表示 5G
     */
    fun is5G(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val telephonyManager = getTelephonyManager(context)
            return telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_NR
        }
        return false
    }

    /**
     * 检查是否为移动网络。
     *
     * @param context 上下文
     * @return true 表示移动网络
     */
    fun isMobileNetwork(context: Context): Boolean {
        val telephonyManager = getTelephonyManager(context)
        return telephonyManager.networkType != TelephonyManager.NETWORK_TYPE_UNKNOWN
    }

    /**
     * 检查 SIM 卡是否就绪。
     *
     * @param context 上下文
     * @return true 表示就绪
     */
    fun isSimReady(context: Context): Boolean {
        val telephonyManager = getTelephonyManager(context)
        return telephonyManager.simState == TelephonyManager.SIM_STATE_READY
    }

    /**
     * 获取 SIM 卡状态。
     *
     * @param context 上下文
     * @return 状态字符串
     */
    fun getSimState(context: Context): String {
        val telephonyManager = getTelephonyManager(context)
        return when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_UNKNOWN -> "未知"
            TelephonyManager.SIM_STATE_ABSENT -> "无SIM卡"
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> "需要PIN码"
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> "需要PUK码"
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> "网络锁定"
            TelephonyManager.SIM_STATE_READY -> "就绪"
            else -> "未知"
        }
    }

    /**
     * 获取国家代码。
     *
     * @param context 上下文
     * @return 国家代码
     */
    fun getNetworkCountryIso(context: Context): String {
        val telephonyManager = getTelephonyManager(context)
        return telephonyManager.networkCountryIso ?: ""
    }

    /**
     * 监听信号强度变化。
     *
     * @param context 上下文
     * @param listener 信号强度回调
     * @return PhoneStateListener
     */
    @Suppress("MissingPermission")
    fun listenSignalStrength(
        context: Context,
        listener: (SignalStrength) -> Unit
    ): PhoneStateListener {
        val telephonyManager = getTelephonyManager(context)

        val phoneListener = object : PhoneStateListener() {
            override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                super.onSignalStrengthsChanged(signalStrength)
                listener(signalStrength)
            }
        }

        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        return phoneListener
    }

    /**
     * 移除信号监听。
     *
     * @param context 上下文
     * @param listener 要移除的监听
     */
    fun removeSignalListener(context: Context, listener: PhoneStateListener) {
        val telephonyManager = getTelephonyManager(context)
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
    }

    /**
     * 检查是否为漫游状态。
     *
     * @param context 上下文
     * @return true 表示漫游
     */
    fun isNetworkRoaming(context: Context): Boolean {
        val telephonyManager = getTelephonyManager(context)
        return telephonyManager.isNetworkRoaming
    }

    /**
     * 拨打电话。
     *
     * @param context 上下文
     * @param phoneNumber 电话号码
     */
    fun makeCall(context: Context, phoneNumber: String) {
        val intent = android.content.Intent(
            Intent.ACTION_DIAL,
            android.net.Uri.parse("tel:$phoneNumber")
        )
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
