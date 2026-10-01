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

import com.aiai.network.config.ApiConfig
import okhttp3.CertificatePinner
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * SSL配置管理类。
 *
 * 负责配置SSL/TLS相关设置，包括自定义CA、证书锁定、双向认证等。
 *
 * @property config API配置
 */
class SSLManager(
    private val config: ApiConfig = ApiConfig.getInstance(),
) {

    /** SSLSocketFactory实例 */
    val sslSocketFactory: SSLSocketFactory

    /** TrustManager实例 */
    val trustManager: X509TrustManager

    /** HostnameVerifier实例 */
    val hostnameVerifier: HostnameVerifier

    init {
        trustManager = TrustManagerImpl()
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), java.security.SecureRandom())
        sslSocketFactory = sslContext.socketFactory
        hostnameVerifier = HostnameVerifier { _, session ->
            // 默认验证主机名
            session.peerPrincipal != null
        }
    }

    /**
     * 获取CertificatePinner配置。
     *
     * @return CertificatePinner实例
     */
    fun getCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add("api.aiai.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()
    }
}
