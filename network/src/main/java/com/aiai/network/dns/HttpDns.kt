/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.dns

/**
 * HTTPDNS实现接口。
 *
 * HTTPDNS通过HTTP协议直接获取域名解析结果，绕过系统DNS，
 * 防止DNS劫持和污染。
 */
interface HttpDns {

    /**
     * 解析域名获取IP地址。
     *
     * @param hostname 域名
     * @return IP地址列表
     */
    fun resolve(hostname: String): List<String>

    /**
     * 预解析多个域名。
     *
     * @param hostnames 域名列表
     */
    fun preResolve(hostnames: List<String>)

    /**
     * 清空缓存。
     */
    fun clearCache()
}
