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
package com.aiai.common.util.crypto

/**
 * Hex（十六进制）编解码工具类。
 */
object HexUtil {

    private const val HEX_CHARS = "0123456789abcdef"

    /** 字节数组转 Hex 字符串（小写）。 */
    fun encode(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val i = b.toInt() and 0xFF
            sb.append(HEX_CHARS[i ushr 4])
            sb.append(HEX_CHARS[i and 0x0F])
        }
        return sb.toString()
    }

    /** Hex 字符串转字节数组。 */
    fun decode(hex: String): ByteArray {
        val clean = hex.replace(" ", "").replace("\n", "")
        val len = clean.length / 2
        val bytes = ByteArray(len)
        for (i in 0 until len) {
            bytes[i] = ((Character.digit(clean[i * 2], 16) shl 4)
                + Character.digit(clean[i * 2 + 1], 16)).toByte()
        }
        return bytes
    }

    /** 字节数组转 Hex 字符串（大写）。 */
    fun encodeUpper(bytes: ByteArray): String {
        return encode(bytes).uppercase()
    }
}
