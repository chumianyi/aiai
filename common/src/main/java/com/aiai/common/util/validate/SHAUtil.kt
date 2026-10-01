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
package com.aiai.common.util.validate

import java.security.MessageDigest

/**
 * SHA 系列散列工具类。
 *
 * 支持 SHA-1 / SHA-256 / SHA-512。
 */
object SHAUtil {

    /** SHA-1。 */
    fun sha1(text: String): String {
        return hash(text, "SHA-1")
    }

    /** SHA-256。 */
    fun sha256(text: String): String {
        return hash(text, "SHA-256")
    }

    /** SHA-512。 */
    fun sha512(text: String): String {
        return hash(text, "SHA-512")
    }

    private fun hash(text: String, algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm).digest(text.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
