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

/**
 * 路由请求 V2。
 */
data class RouteRequestV2(val uri: Uri)

/**
 * 路由拦截器 V2。
 */
interface RouteInterceptorV2 {
    fun intercept(chain: Chain): Boolean
    interface Chain {
        val request: RouteRequestV2
        fun proceed(): Boolean
    }
}

/**
 * 路由框架 V2。
 */
object RouterV2 {
    private lateinit var context: Context
    private val interceptors = mutableListOf<RouteInterceptorV2>()

    fun init(context: Context) { this.context = context.applicationContext }
    fun register(interceptor: RouteInterceptorV2) { interceptors.add(interceptor) }
}
