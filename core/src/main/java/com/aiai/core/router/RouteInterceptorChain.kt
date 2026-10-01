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
import android.os.Bundle

/**
 * 路由拦截器链。
 *
 * 按优先级顺序执行所有拦截器。
 */
class RouteInterceptorChain(
    private val interceptors: List<RouteInterceptor>
) {

    /**
     * 执行拦截链。
     *
     * @return true 表示被拦截（不跳转），false 表示放行
     */
    fun process(context: Context, path: String, extras: Bundle?): Boolean {
        val sorted = interceptors.sortedByDescending { it.priority }
        for (interceptor in sorted) {
            if (interceptor.intercept(context, path, extras)) {
                return true // 被拦截
            }
        }
        return false // 全部放行
    }
}
