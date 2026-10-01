/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.aiai.core.util.LogUtil
import com.aiai.core.util.ThemeUtil
import com.aiai.core.util.LanguageUtil
import com.aiai.core.util.CrashHandler
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * 爱Ai应用全局Application类
 *
 * 负责：
 * - Hilt依赖注入初始化
 * - MMKV键值存储初始化
 * - 主题模式初始化
 * - 语言环境初始化
 * - 全局崩溃捕获初始化
 * - 应用生命周期监听注册
 * - WorkManager配置（Hilt Worker）
 */
@HiltAndroidApp
class AiAiApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    @Inject
    lateinit var globalExceptionHandler: GlobalExceptionHandler

    @Inject
    lateinit var featureFlagManager: FeatureFlagManager

    companion object {
        private const val TAG = "AiAiApplication"

        lateinit var instance: AiAiApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LogUtil.d(TAG, "Application onCreate start")

        // 1. MMKV初始化（必须在其他存储之前）
        initMMKV()

        // 2. 全局异常处理器
        initCrashHandler()

        // 3. 主题初始化
        initTheme()

        // 4. 语言初始化
        initLanguage()

        // 5. 注册生命周期观察者
        registerActivityLifecycleCallbacks(appLifecycleObserver)
        registerComponentCallbacks(appLifecycleObserver)

        // 6. 功能开关加载
        featureFlagManager.loadFeatureFlags()

        // 7. 通知渠道创建（Android O+）
        NotificationHelper.createNotificationChannels(this)

        LogUtil.d(TAG, "Application onCreate end")
    }

    /**
     * 初始化MMKV
     */
    private fun initMMKV() {
        val mmkvDir = MMKV.initialize(this)
        LogUtil.d(TAG, "MMKV initialized at: $mmkvDir")
    }

    /**
     * 初始化全局崩溃处理器
     */
    private fun initCrashHandler() {
        CrashHandler.install(globalExceptionHandler)
        LogUtil.d(TAG, "CrashHandler installed")
    }

    /**
     * 初始化应用主题模式
     */
    private fun initTheme() {
        val mode = ThemeUtil.getNightMode(this)
        AppCompatDelegate.setDefaultNightMode(mode)
        LogUtil.d(TAG, "Theme night mode: $mode")
    }

    /**
     * 初始化应用语言环境
     */
    private fun initLanguage() {
        val locale = LanguageUtil.getSavedLocale(this)
        LanguageUtil.applyLocale(this, locale)
        LogUtil.d(TAG, "Language applied: $locale")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    override fun attachBaseContext(base: Context) {
        val locale = LanguageUtil.getSavedLocale(base)
        val context = LanguageUtil.wrap(base, locale)
        super.attachBaseContext(context)
    }
}
