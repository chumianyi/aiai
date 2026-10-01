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

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 * 自定义TrustManager实现。
 *
 * 默认信任系统CA证书，开发环境下可配置为信任所有证书。
 */
class TrustManagerImpl : X509TrustManager {

    private val defaultTrustManager: X509TrustManager by lazy {
        val factory = javax.net.ssl.TrustManagerFactory.getInstance(
            javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm()
        )
        factory.init(null)
        factory.trustManagers.filterIsInstance<X509TrustManager>().first()
    }

    /** 是否信任所有证书（仅调试模式） */
    var trustAllCertificates: Boolean = false

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        if (!trustAllCertificates) {
            defaultTrustManager.checkClientTrusted(chain, authType)
        }
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        if (!trustAllCertificates) {
            defaultTrustManager.checkServerTrusted(chain, authType)
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return defaultTrustManager.acceptedIssuers
    }
}
