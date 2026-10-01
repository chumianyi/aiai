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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.util.Base64

/**
 * Base64 工具类。
 */
object Base64Util {

    /** 编码。 */
    fun encode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }

    /** 解码。 */
    fun decode(data: String): ByteArray {
        return Base64.decode(data, Base64.NO_WRAP)
    }

    /** 字符串编码。 */
    fun encodeString(text: String): String {
        return encode(text.toByteArray(Charsets.UTF_8))
    }

    /** 字符串解码。 */
    fun decodeToString(data: String): String {
        return String(decode(data), Charsets.UTF_8)
    }
}
