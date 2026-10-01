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
package com.aiai.core

import android.app.Application
import android.content.Context
import com.aiai.common.util.other.Logger
import com.aiai.common.util.storage.MMKVManager
import com.aiai.common.util.storage.SPManager
import com.aiai.core.crash.CrashCollector
import com.aiai.core.event.EventBus
import com.aiai.core.init.AppInitializer
import com.aiai.core.lifecycle.AppLifecycleObserver
import com.aiai.core.manager.ActivityStackManager
import com.aiai.core.router.Router
import com.aiai.core.router.LoginInterceptor
import com.aiai.core.router.AlreadyLoginInterceptor
import com.aiai.core.router.DebugInterceptor
import com.aiai.core.router.PrivacyInterceptor
import com.aiai.core.router.NetworkInterceptor
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用基类 Application。
 *
 * 完整初始化流程：
 * 1. Hilt 依赖注入
 * 2. MMKV / SP 存储
 * 3. 日志系统
 * 4. 崩溃收集
 * 5. 路由框架
 * 6. 事件总线
 * 7. 应用前后台监听
 * 8. Activity 栈管理
 * 9. 启动初始化器
 */
@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 1. 存储初始化
        SPManager.init(this)
        MMKVManager.init(this)

        // 2. 日志
        Logger.enabled = BuildConfig.DEBUG
        Logger.tag = "AiAi"
        Logger.d("Application onCreate")

        // 3. 崩溃收集
        CrashCollector.install(this)

        // 4. 路由初始化
        Router.init(this)
        Router.registerInterceptor(PrivacyInterceptor())
        Router.registerInterceptor(LoginInterceptor())
        Router.registerInterceptor(AlreadyLoginInterceptor())
        Router.registerInterceptor(NetworkInterceptor())
        Router.registerInterceptor(DebugInterceptor())

        // 5. Activity 栈监听
        registerActivityLifecycleCallbacks(ActivityStackManager)

        // 6. 前后台监听
        registerActivityLifecycleCallbacks(AppLifecycleObserver())

        // 7. 事件总线初始化
        EventBus

        // 8. 启动初始化器
        AppInitializer.init(this)

        Logger.d("Application onCreate done")
    }

    companion object {
        lateinit var instance: BaseApplication
            private set

        fun appContext(): Context = instance.applicationContext
    }
}
