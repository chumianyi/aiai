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

/**
 * 路由拦截器接口。
 *
 * 用于在路由跳转前后执行拦截逻辑，如登录检查、权限检查等。
 */
interface RouteInterceptor {

    /**
     * 拦截路由请求。
     *
     * @param context 当前 Context
     * @param path 路由路径
     * @param extras 传递的参数
     * @return true 表示拦截（不跳转），false 表示放行
     */
    fun intercept(context: Context, path: String, extras: Bundle?): Boolean

    /** 拦截器优先级（数字越大越先执行）。 */
    val priority: Int get() = 0
}
