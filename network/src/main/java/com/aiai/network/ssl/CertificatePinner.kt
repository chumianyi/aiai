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
package com.aiai.network.ssl

import okhttp3.CertificatePinner

/**
 * 证书锁定配置。
 *
 * 通过锁定证书公钥指纹，防止中间人攻击。
 * 即使CA被攻破，攻击者也无法伪造证书。
 */
object CertificatePinnerConfig {

    /** 已锁定的证书指纹映射 */
    private val pinnedCertificates = mutableMapOf<String, List<String>>()

    init {
        // 添加默认锁定
        pinnedCertificates["api.aiai.com"] = listOf(
            "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
            "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=",
        )
        pinnedCertificates["ws.aiai.com"] = listOf(
            "sha256/CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC=",
        )
    }

    /**
     * 构建CertificatePinner。
     *
     * @return CertificatePinner实例
     */
    fun build(): CertificatePinner {
        val builder = CertificatePinner.Builder()
        pinnedCertificates.forEach { (hostname, pins) ->
            pins.forEach { pin ->
                builder.add(hostname, pin)
            }
        }
        return builder.build()
    }

    /**
     * 添加证书锁定。
     *
     * @param hostname 域名
     * @param pins 证书指纹列表
     */
    fun addPin(hostname: String, pins: List<String>) {
        pinnedCertificates[hostname] = pins
    }

    /**
     * 移除证书锁定。
     *
     * @param hostname 域名
     */
    fun removePin(hostname: String) {
        pinnedCertificates.remove(hostname)
    }
}
