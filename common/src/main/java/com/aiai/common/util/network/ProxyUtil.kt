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

import java.net.InetSocketAddress
import java.net.Proxy

/**
 * 代理工具类。
 */
object ProxyUtil {

    /** 创建 HTTP 代理。 */
    fun httpProxy(host: String, port: Int): Proxy {
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
    }

    /** 创建 SOCKS 代理。 */
    fun socksProxy(host: String, port: Int): Proxy {
        return Proxy(Proxy.Type.SOCKS, InetSocketAddress(host, port))
    }

    /** 获取系统默认代理。 */
    fun systemDefault(): Proxy = Proxy.NO_PROXY
}
