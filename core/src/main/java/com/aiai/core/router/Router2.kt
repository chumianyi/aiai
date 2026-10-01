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
import android.content.Intent
import android.net.Uri
import com.aiai.common.util.other.Logger

/**
 * 路由请求。
 */
data class RouteRequest(
    val uri: Uri,
    val flags: Int = 0,
    val extras: android.os.Bundle? = null,
    val requestCode: Int = -1
)

/**
 * 路由拦截器链。
 */
interface RouteInterceptorChain {
    val request: RouteRequest
    fun proceed(): Boolean
}

/**
 * 路由拦截器接口。
 */
interface RouteInterceptor {
    fun intercept(chain: RouteInterceptorChain): Boolean
}

/**
 * 路由框架。
 */
object Router {

    private lateinit var context: Context
    private val interceptors = mutableListOf<RouteInterceptor>()

    fun init(context: Context) {
        this.context = context.applicationContext
        Logger.d("Router initialized")
    }

    fun registerInterceptor(interceptor: RouteInterceptor) {
        interceptors.add(interceptor)
    }

    fun buildRequest(uri: String): RouteRequest {
        return RouteRequest(uri = Uri.parse(uri))
    }

    fun navigation(request: RouteRequest): Boolean {
        Logger.d("Router navigate: ${request.uri}")
        return try {
            val intent = Intent(Intent.ACTION_VIEW, request.uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (request.flags != 0) flags = request.flags
                request.extras?.let { putExtras(it) }
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Logger.e("Router", e)
            false
        }
    }
}
