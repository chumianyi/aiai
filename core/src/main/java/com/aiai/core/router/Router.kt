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
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.core.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.aiai.core.AppConfig

/**
 * 路由框架。
 *
 * 基于 Intent 的路由跳转，支持：
 * - 路径 → Activity 映射
 * - 参数传递
 * - 拦截器
 * - 转场动画
 * - 结果回调
 */
object Router {

    private val routeMap = mutableMapOf<String, Class<*>>()
    private val interceptors = mutableListOf<RouteInterceptor>()
    private lateinit var appContext: Context

    /** 初始化路由（在 Application 中调用）。 */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /** 注册路由。 */
    fun registerRoute(path: String, clazz: Class<*>) {
        routeMap[path] = clazz
    }

    /** 注册拦截器。 */
    fun registerInterceptor(interceptor: RouteInterceptor) {
        interceptors.add(interceptor)
    }

    /**
     * 跳转路由。
     *
     * @param context 当前 Context
     * @param path 路由路径
     * @param extras 传递参数
     * @param activityOptions 转场动画
     */
    fun navigate(
        context: Context,
        path: String,
        extras: Bundle? = null,
        activityOptions: ActivityOptionsCompat? = null
    ): Boolean {
        // 执行拦截器
        val chain = RouteInterceptorChain(interceptors)
        if (chain.process(context, path, extras)) return false

        val targetClass = routeMap[path] ?: return false
        val intent = Intent(context, targetClass).apply {
            extras?.let { putExtras(it) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent, activityOptions?.toBundle())
        return true
    }

    /** 构建 Intent。 */
    fun buildIntent(path: String, extras: Bundle? = null): Intent? {
        val targetClass = routeMap[path] ?: return null
        return Intent(appContext, targetClass).apply {
            extras?.let { putExtras(it) }
        }
    }
}

/**
 * 登录拦截器：需要登录的页面检查登录状态。
 */
class LoginInterceptor : RouteInterceptor {
    override fun intercept(context: Context, path: String, extras: Bundle?): Boolean {
        val needLoginPaths = setOf(
            "/chat/conversation", "/chat/new", "/user/profile",
            "/settings/account", "/settings/storage"
        )
        if (path in needLoginPaths && !AppConfig.isLoggedIn) {
            Router.navigate(context, "/login")
            return true
        }
        return false
    }

    override val priority: Int get() = 100
}

/**
 * 登录页拦截：已登录用户访问登录页直接跳主页。
 */
class AlreadyLoginInterceptor : RouteInterceptor {
    override fun intercept(context: Context, path: String, extras: Bundle?): Boolean {
        if (path == "/login" && AppConfig.isLoggedIn) {
            Router.navigate(context, "/main")
            return true
        }
        return false
    }

    override val priority: Int get() = 90
}

/**
 * 调试拦截器：打印路由日志。
 */
class DebugInterceptor : RouteInterceptor {
    override fun intercept(context: Context, path: String, extras: Bundle?): Boolean {
        if (AppConfig.isDebug) {
            android.util.Log.d("Router", "Navigating to: $path")
        }
        return false // 不拦截
    }

    override val priority: Int get() = 10
}

/**
 * 隐私协议拦截：未同意隐私协议的用户拦截。
 */
class PrivacyInterceptor : RouteInterceptor {
    override fun intercept(context: Context, path: String, extras: Bundle?): Boolean {
        val agreed = com.aiai.common.util.storage.MMKVManager.getBoolean("privacy_agreed", false)
        val whiteList = setOf("/splash", "/agreement", "/privacy_policy")
        if (!agreed && path !in whiteList) {
            Router.navigate(context, "/agreement")
            return true
        }
        return false
    }

    override val priority: Int get() = 200
}

/**
 * 网络状态拦截：需要网络的页面检查网络。
 */
class NetworkInterceptor : RouteInterceptor {
    override fun intercept(context: Context, path: String, extras: Bundle?): Boolean {
        val needNetworkPaths = setOf("/chat/conversation", "/chat/new")
        if (path in needNetworkPaths) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager
            val isConnected = cm?.activeNetwork != null
            if (!isConnected) {
                android.widget.Toast.makeText(context, "网络不可用", android.widget.Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }

    override val priority: Int get() = 50
}
