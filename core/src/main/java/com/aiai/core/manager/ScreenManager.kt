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

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.view.WindowManager

/**
 * 屏幕管理器。
 *
 * 提供亮度、超时、旋转、截屏、录屏等功能。
 */
class ScreenManager(private val activity: Activity) {

    /**
     * 设置屏幕亮度。
     *
     * @param brightness 亮度值（0-1）
     */
    fun setBrightness(brightness: Float) {
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = brightness.coerceIn(0f, 1f)
        activity.window.attributes = layoutParams
    }

    /**
     * 恢复默认亮度。
     */
    fun resetBrightness() {
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        activity.window.attributes = layoutParams
    }

    /**
     * 获取当前屏幕亮度。
     *
     * @return 亮度值（0-1）
     */
    fun getBrightness(): Float {
        return activity.window.attributes.screenBrightness
    }

    /**
     * 设置屏幕超时时间。
     *
     * @param timeout 超时时间（毫秒）
     */
    fun setScreenTimeout(timeout: Long) {
        Settings.System.putInt(
            activity.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            timeout.toInt()
        )
    }

    /**
     * 获取屏幕超时时间。
     *
     * @return 超时时间（毫秒）
     */
    fun getScreenTimeout(): Long {
        return try {
            Settings.System.getInt(
                activity.contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT
            ).toLong()
        } catch (e: Settings.SettingNotFoundException) {
            30000L
        }
    }

    /**
     * 保持屏幕常亮。
     */
    fun keepScreenOn() {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /** 取消屏幕常亮。 */
    fun clearKeepScreenOn() {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /** 设置竖屏。 */
    fun setPortrait() {
        activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /** 设置横屏。 */
    fun setLandscape() {
        activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /** 设置自动旋转。 */
    fun setAutoRotate() {
        activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    /**
     * 禁止截屏。
     */
    fun preventScreenshot() {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    /** 允许截屏。 */
    fun allowScreenshot() {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    /**
     * 获取屏幕宽度。
     *
     * @return 屏幕宽度（像素）
     */
    fun getScreenWidth(): Int {
        return activity.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度。
     *
     * @return 屏幕高度（像素）
     */
    fun getScreenHeight(): Int {
        return activity.resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕密度。
     *
     * @return 屏幕密度
     */
    fun getDensity(): Float {
        return activity.resources.displayMetrics.density
    }
}
