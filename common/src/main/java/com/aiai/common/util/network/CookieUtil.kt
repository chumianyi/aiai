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
package com.aiai.common.util.network

import java.net.CookieManager
import java.net.CookieStore
import java.net.HttpCookie

/**
 * Cookie 管理工具类。
 */
object CookieUtil {

    private val cookieStore: CookieStore = java.net.CookieManager().cookieStore

    /** 添加 Cookie。 */
    fun add(uri: String, cookie: HttpCookie) {
        cookieStore.add(java.net.URI.create(uri), cookie)
    }

    /** 获取指定 URI 的 Cookie 列表。 */
    fun get(uri: String): List<HttpCookie> {
        return cookieStore.get(java.net.URI.create(uri))
    }

    /** 清除所有 Cookie。 */
    fun clear() {
        cookieStore.removeAll()
    }
}
