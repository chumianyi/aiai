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
package com.aiai.common.ext

import android.util.Base64

/**
 * ByteArray 相关扩展函数集合。
 *
 * 提供 Hex 转换、Base64、拼接、截取等能力。
 */

/** ByteArray 转 Hex 字符串（小写）。 */
fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

/** Hex 字符串转 ByteArray。 */
fun String.hexToBytes(): ByteArray {
    val len = length / 2
    val bytes = ByteArray(len)
    for (i in 0 until len) {
        bytes[i] = ((Character.digit(this[i * 2], 16) shl 4)
            + Character.digit(this[i * 2 + 1], 16)).toByte()
    }
    return bytes
}

/** ByteArray 转 Base64 字符串。 */
fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

/** Base64 字符串转 ByteArray。 */
fun String.base64ToBytes(): ByteArray {
    return Base64.decode(this, Base64.NO_WRAP)
}

/** 拼接两个 ByteArray。 */
fun ByteArray.concat(other: ByteArray): ByteArray {
    val result = ByteArray(size + other.size)
    System.arraycopy(this, 0, result, 0, size)
    System.arraycopy(other, 0, result, size, other.size)
    return result
}

/** 截取 ByteArray 子数组。 */
fun ByteArray.sub(start: Int, end: Int): ByteArray {
    return copyOfRange(start, end)
}

/** 转 UTF-8 字符串。 */
fun ByteArray.toUtf8String(): String = toString(Charsets.UTF_8)

/** 转 ISO-8859-1 字符串。 */
fun ByteArray.toLatin1String(): String = toString(Charsets.ISO_8859_1)

/** 比较两个 ByteArray 是否内容相等（时间安全）。 */
fun ByteArray.constantTimeEquals(other: ByteArray): Boolean {
    if (size != other.size) return false
    var result = 0
    for (i in indices) result = result or (this[i].toInt() xor other[i].toInt())
    return result == 0
}
