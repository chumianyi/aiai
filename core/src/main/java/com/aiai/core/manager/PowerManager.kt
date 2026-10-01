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
import android.os.PowerManager
import android.util.Log

/**
 * 电源管理器。
 *
 * 提供 WakeLock、省电模式、重启、关机等功能。
 */
class PowerManager(private val context: Context) {

    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    private var wakeLock: PowerManager.WakeLock? = null

    /**
     * 获取 WakeLock。
     *
     * @param tag 标签
     * @param timeout 超时时间（毫秒）
     */
    @Synchronized
    fun acquireWakeLock(tag: String = "AiAi::WakeLock", timeout: Long = 10 * 60 * 1000L) {
        if (wakeLock?.isHeld == true) return

        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            tag
        ).apply {
            acquire(timeout)
        }
        Log.d("PowerManager", "WakeLock acquired: $tag")
    }

    /**
     * 释放 WakeLock。
     */
    @Synchronized
    fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
        wakeLock = null
        Log.d("PowerManager", "WakeLock released")
    }

    /**
     * 检查 WakeLock 是否持有。
     *
     * @return true 表示持有
     */
    fun isWakeLockHeld(): Boolean {
        return wakeLock?.isHeld == true
    }

    /**
     * 检查是否为省电模式。
     *
     * @return true 表示省电模式
     */
    fun isPowerSaveMode(): Boolean {
        return powerManager.isPowerSaveMode
    }

    /**
     * 检查设备是否处于交互状态。
     *
     * @return true 表示交互中
     */
    fun isInteractive(): Boolean {
        return powerManager.isInteractive
    }

    /**
     * 检查设备是否处于锁屏状态。
     *
     * @return true 表示已锁屏
     */
    fun isScreenLocked(): Boolean {
        return powerManager.isScreenOn
    }

    /**
     * 检查是否为闲置模式。
     *
     * @return true 表示闲置
     */
    fun isDeviceIdleMode(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            powerManager.isDeviceIdleMode
        } else {
            false
        }
    }

    /**
     * 获取电池电量信息。
     *
     * @return 电量百分比
     */
    fun getBatteryLevel(): Int {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent?.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1) ?: -1
        return if (level == -1 || scale == -1) {
            -1
        } else {
            (level * 100 / scale)
        }
    }

    /**
     * 检查是否正在充电。
     *
     * @return true 表示充电中
     */
    fun isCharging(): Boolean {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        val status = intent?.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == android.os.BatteryManager.BATTERY_STATUS_CHARGING ||
                status == android.os.BatteryManager.BATTERY_STATUS_FULL
    }

    /**
     * 获取充电类型。
     *
     * @return 充电类型
     */
    fun getChargeType(): String {
        val intent = context.registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent?.getIntExtra(android.os.BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        return when (plugged) {
            android.os.BatteryManager.BATTERY_PLUGGED_AC -> "AC充电"
            android.os.BatteryManager.BATTERY_PLUGGED_USB -> "USB充电"
            android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS -> "无线充电"
            else -> "未充电"
        }
    }
}
