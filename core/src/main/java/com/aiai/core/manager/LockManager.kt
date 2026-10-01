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

import android.util.Log

/**
 * 应用锁管理器。
 *
 * 提供应用锁设置、解锁验证、自动锁定等功能。
 */
object LockManager {

    private const val TAG = "LockManager"

    /**
     * 锁定方式枚举。
     */
    enum class LockType(val displayName: String) {
        /** 密码 */
        PASSWORD("密码"),
        /** 指纹 */
        FINGERPRINT("指纹"),
        /** 面容 */
        FACE("面容"),
        /** 无 */
        NONE("无")
    }

    private var lockType: LockType = LockType.NONE
    private var password: String = ""
    private var autoLockDelay: Long = 300000L // 5分钟
    private var lastBackgroundTime: Long = 0L
    private var isLocked: Boolean = false

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "LockManager initialized")
    }

    /**
     * 设置锁定方式。
     *
     * @param type 锁定方式
     */
    fun setLockType(type: LockType) {
        lockType = type
        Log.d(TAG, "Lock type set: ${type.displayName}")
    }

    /**
     * 获取锁定方式。
     *
     * @return 锁定方式
     */
    fun getLockType(): LockType = lockType

    /**
     * 是否启用应用锁。
     *
     * @return 是否启用
     */
    fun isLockEnabled(): Boolean {
        return lockType != LockType.NONE
    }

    /**
     * 设置密码。
     *
     * @param pwd 密码
     * @return 是否成功
     */
    fun setPassword(pwd: String): Boolean {
        if (pwd.length < 4) {
            Log.w(TAG, "Password too short")
            return false
        }
        password = pwd
        Log.d(TAG, "Password set")
        return true
    }

    /**
     * 验证密码。
     *
     * @param pwd 密码
     * @return 是否正确
     */
    fun verifyPassword(pwd: String): Boolean {
        return password == pwd
    }

    /**
     * 锁定应用。
     */
    fun lock() {
        isLocked = true
        Log.d(TAG, "App locked")
    }

    /**
     * 解锁应用。
     */
    fun unlock() {
        isLocked = false
        lastBackgroundTime = 0L
        Log.d(TAG, "App unlocked")
    }

    /**
     * 是否已锁定。
     *
     * @return 是否锁定
     */
    fun isLocked(): Boolean {
        if (!isLockEnabled()) return false
        if (!isLocked) return false

        // 检查是否需要自动解锁
        if (lastBackgroundTime > 0) {
            val now = System.currentTimeMillis()
            if (now - lastBackgroundTime > autoLockDelay) {
                return true
            }
        }
        return true
    }

    /**
     * 应用进入后台。
     */
    fun onEnterBackground() {
        lastBackgroundTime = System.currentTimeMillis()
    }

    /**
     * 应用回到前台。
     */
    fun onEnterForeground() {
        if (isLockEnabled() && lastBackgroundTime > 0) {
            val now = System.currentTimeMillis()
            if (now - lastBackgroundTime > autoLockDelay) {
                isLocked = true
                Log.d(TAG, "Auto lock triggered")
            }
        }
        lastBackgroundTime = 0L
    }

    /**
     * 设置自动锁定延迟。
     *
     * @param delay 延迟（毫秒）
     */
    fun setAutoLockDelay(delay: Long) {
        autoLockDelay = delay
        Log.d(TAG, "Auto lock delay set: $delay ms")
    }

    /**
     * 获取自动锁定延迟。
     *
     * @return 延迟（毫秒）
     */
    fun getAutoLockDelay(): Long = autoLockDelay

    /**
     * 关闭应用锁。
     */
    fun disableLock() {
        lockType = LockType.NONE
        password = ""
        isLocked = false
        Log.d(TAG, "Lock disabled")
    }
}
