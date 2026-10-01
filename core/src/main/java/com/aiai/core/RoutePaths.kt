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
package com.aiai.core

/**
 * 路由路径常量定义。
 *
 * 所有页面路由路径统一在此管理。
 */
object RoutePaths {

    // region 启动 & 主页面
    const val SPLASH = "/splash"
    const val MAIN = "/main"
    const val HOME = "/home"
    // endregion

    // region 聊天
    const val CHAT_LIST = "/chat/list"
    const val CHAT_CONVERSATION = "/chat/conversation"
    const val CHAT_NEW = "/chat/new"
    const val CHAT_DETAIL = "/chat/detail"
    const val CHAT_SESSION_MANAGE = "/chat/session/manage"
    const val CHAT_HISTORY = "/chat/history"
    // endregion

    // region 设置
    const val SETTINGS = "/settings"
    const val SETTINGS_ACCOUNT = "/settings/account"
    const val SETTINGS_APPEARANCE = "/settings/appearance"
    const val SETTINGS_LANGUAGE = "/settings/language"
    const val SETTINGS_ABOUT = "/settings/about"
    const val SETTINGS_PRIVACY = "/settings/privacy"
    const val SETTINGS_NOTIFICATION = "/settings/notification"
    const val SETTINGS_STORAGE = "/settings/storage"
    const val SETTINGS_ABOUT_VERSION = "/settings/about/version"
    // endregion

    // region 登录 & 注册
    const val LOGIN = "/login"
    const val REGISTER = "/register"
    const val FORGOT_PASSWORD = "/forgot_password"
    // endregion

    // region 用户
    const val USER_PROFILE = "/user/profile"
    const val USER_EDIT = "/user/edit"
    const val USER_AVATAR = "/user/avatar"
    // endregion

    // region WebView
    const val WEBVIEW = "/webview"
    // endregion

    // region 其他
    const val SEARCH = "/search"
    const val ABOUT = "/about"
    const val FEEDBACK = "/feedback"
    const val HELP = "/help"
    const val AGREEMENT = "/agreement"
    const val PRIVACY_POLICY = "/privacy_policy"
    // endregion
}
