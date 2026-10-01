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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

/**
 * 路由管理器。
 *
 * 提供页面路由、深链接处理、路由拦截等功能。
 */
object RouterManager {

    private const val TAG = "RouterManager"

    /** 路由路径常量。 */
    object Path {
        const val SPLASH = "/app/splash"
        const val MAIN = "/app/main"
        const val CHAT = "/chat/chat"
        const val CHAT_DETAIL = "/chat/detail"
        const val SETTINGS = "/settings/settings"
        const val SETTINGS_ACCOUNT = "/settings/account"
        const val SETTINGS_APPEARANCE = "/settings/appearance"
        const val SETTINGS_ABOUT = "/settings/about"
        const val WEBVIEW = "/app/webview"
        const val LOGIN = "/auth/login"
        const val REGISTER = "/auth/register"
    }

    private val routes = mutableMapOf<String, Class<*>>()
    private val interceptors = mutableListOf<RouterInterceptor>()

    /**
     * 路由拦截器接口。
     */
    interface RouterInterceptor {
        /**
         * 拦截路由。
         *
         * @param path 路由路径
         * @param extras 额外参数
         * @return 是否拦截（true=拦截，不继续跳转）
         */
        fun intercept(path: String, extras: Bundle?): Boolean
    }

    /**
     * 注册路由。
     *
     * @param path 路由路径
     * @param activity Activity 类
     */
    fun register(path: String, activity: Class<*>) {
        routes[path] = activity
        Log.d(TAG, "Registered route: $path -> ${activity.simpleName}")
    }

    /**
     * 添加拦截器。
     *
     * @param interceptor 拦截器
     */
    fun addInterceptor(interceptor: RouterInterceptor) {
        interceptors.add(interceptor)
    }

    /**
     * 移除拦截器。
     *
     * @param interceptor 拦截器
     */
    fun removeInterceptor(interceptor: RouterInterceptor) {
        interceptors.remove(interceptor)
    }

    /**
     * 路由跳转。
     *
     * @param context 上下文
     * @param path 路由路径
     * @param extras 额外参数
     * @param requestCode 请求码（可选，用于 startActivityForResult）
     * @return 是否成功
     */
    fun navigate(
        context: Context,
        path: String,
        extras: Bundle? = null,
        requestCode: Int? = null
    ): Boolean {
        // 检查拦截器
        interceptors.forEach { interceptor ->
            if (interceptor.intercept(path, extras)) {
                Log.d(TAG, "Route $path intercepted")
                return false
            }
        }

        val activityClass = routes[path]
        if (activityClass == null) {
            Log.e(TAG, "Route not found: $path")
            return false
        }

        val intent = Intent(context, activityClass)
        extras?.let { intent.putExtras(it) }

        return try {
            if (requestCode != null) {
                if (context is android.app.Activity) {
                    context.startActivityForResult(intent, requestCode)
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            } else {
                context.startActivity(intent)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Navigate failed: $path", e)
            false
        }
    }

    /**
     * 打开网页。
     *
     * @param context 上下文
     * @param url 网页地址
     * @return 是否成功
     */
    fun openWeb(context: Context, url: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Open web failed: $url", e)
            false
        }
    }

    /**
     * 拨打电话。
     *
     * @param context 上下文
     * @param phoneNumber 电话号码
     * @return 是否成功
     */
    fun callPhone(context: Context, phoneNumber: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Call phone failed: $phoneNumber", e)
            false
        }
    }

    /**
     * 发送邮件。
     *
     * @param context 上下文
     * @param email 邮箱地址
     * @param subject 主题
     * @return 是否成功
     */
    fun sendEmail(context: Context, email: String, subject: String = ""): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Send email failed: $email", e)
            false
        }
    }

    /**
     * 分享文本。
     *
     * @param context 上下文
     * @param text 分享内容
     * @param title 分享标题
     * @return 是否成功
     */
    fun shareText(context: Context, text: String, title: String = "分享"): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, title))
            true
        } catch (e: Exception) {
            Log.e(TAG, "Share text failed", e)
            false
        }
    }

    /**
     * 获取路由总数。
     *
     * @return 路由数量
     */
    fun routeCount(): Int = routes.size

    /**
     * 检查路由是否存在。
     *
     * @param path 路由路径
     * @return 是否存在
     */
    fun hasRoute(path: String): Boolean = routes.containsKey(path)

    /**
     * 清除所有路由。
     */
    fun clear() {
        routes.clear()
        interceptors.clear()
    }
}
