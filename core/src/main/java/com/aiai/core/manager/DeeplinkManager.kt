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

import android.net.Uri
import android.util.Log

/**
 * 深链接管理器。
 *
 * 提供深链接解析、路由映射、参数处理等功能。
 */
object DeeplinkManager {

    private const val TAG = "DeeplinkManager"
    private const val SCHEME = "aiai"
    private const val HOST = "app.aiai.com"

    private val deeplinkHandlers = mutableMapOf<String, (Map<String, String>) -> Unit>()

    /**
     * 初始化。
     */
    fun init() {
        // 注册默认处理
        registerHandler("chat") { params ->
            Log.d(TAG, "Handle chat deeplink: $params")
        }
        registerHandler("settings") { params ->
            Log.d(TAG, "Handle settings deeplink: $params")
        }
        registerHandler("profile") { params ->
            Log.d(TAG, "Handle profile deeplink: $params")
        }
        Log.d(TAG, "DeeplinkManager initialized")
    }

    /**
     * 注册深链接处理。
     *
     * @param path 路径
     * @param handler 处理器
     */
    fun registerHandler(path: String, handler: (Map<String, String>) -> Unit) {
        deeplinkHandlers[path] = handler
        Log.d(TAG, "Registered deeplink handler: $path")
    }

    /**
     * 处理深链接。
     *
     * @param uri 深链接 URI
     * @return 是否成功处理
     */
    fun handleDeeplink(uri: Uri): Boolean {
        Log.d(TAG, "Handling deeplink: $uri")

        val path = uri.path?.trimStart('/') ?: return false
        val handler = deeplinkHandlers[path] ?: run {
            Log.w(TAG, "No handler for path: $path")
            return false
        }

        // 解析参数
        val params = mutableMapOf<String, String>()
        uri.queryParameterNames.forEach { key ->
            uri.getQueryParameter(key)?.let { value ->
                params[key] = value
            }
        }

        handler(params)
        return true
    }

    /**
     * 构建深链接。
     *
     * @param path 路径
     * @param params 参数
     * @return 深链接 URI
     */
    fun buildDeeplink(path: String, params: Map<String, String> = emptyMap(): Uri {
        val builder = Uri.Builder()
            .scheme(SCHEME)
            .authority(HOST)
            .path(path)

        params.forEach { (key, value) ->
            builder.appendQueryParameter(key, value)
        }

        return builder.build()
    }

    /**
     * 获取已注册的路径。
     *
     * @return 路径列表
     */
    fun getRegisteredPaths(): List<String> {
        return deeplinkHandlers.keys.toList()
    }

    /**
     * 检查是否支持路径。
     *
     * @param path 路径
     * @return 是否支持
     */
    fun supportsPath(path: String): Boolean {
        return deeplinkHandlers.containsKey(path)
    }
}
