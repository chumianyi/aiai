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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.router

import android.content.Context
import android.net.Uri
import com.aiai.common.util.other.Logger

/**
 * 路由拦截器 - 隐私协议拦截。
 */
class PrivacyInterceptor : RouteInterceptor {
    override fun intercept(chain: RouteInterceptorChain): Boolean {
        val uri = chain.request.uri
        Logger.d("PrivacyInterceptor: $uri")
        // TODO: 检查隐私协议是否已同意
        return false
    }
}

/**
 * 路由拦截器 - 登录拦截。
 */
class LoginInterceptor : RouteInterceptor {
    override fun intercept(chain: RouteInterceptorChain): Boolean {
        val uri = chain.request.uri
        Logger.d("LoginInterceptor: $uri")
        // TODO: 检查登录状态，未登录跳转登录页
        return false
    }
}

/**
 * 路由拦截器 - 已登录拦截（已登录不可访问登录页）。
 */
class AlreadyLoginInterceptor : RouteInterceptor {
    override fun intercept(chain: RouteInterceptorChain): Boolean {
        Logger.d("AlreadyLoginInterceptor")
        return false
    }
}

/**
 * 路由拦截器 - 网络拦截。
 */
class NetworkInterceptor : RouteInterceptor {
    override fun intercept(chain: RouteInterceptorChain): Boolean {
        Logger.d("NetworkInterceptor")
        return false
    }
}

/**
 * 路由拦截器 - Debug 拦截。
 */
class DebugInterceptor : RouteInterceptor {
    override fun intercept(chain: RouteInterceptorChain): Boolean {
        Logger.d("DebugInterceptor: ${chain.request.uri}")
        return false
    }
}
