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
package com.aiai.common.ext

import android.util.Patterns
import java.util.regex.Pattern

/**
 * String 扩展函数集（扩展版）。
 */

/** 是否为空。 */
fun String?.isEmpty(): Boolean {
    return this == null || this.isEmpty()
}

/** 是否为空白。 */
fun String?.isBlank(): Boolean {
    return this == null || this.isBlank()
}

/** 是否非空。 */
fun String?.isNotEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

/** 手机号校验。 */
fun String.isPhone(): Boolean {
    return matches(Regex("^1[3-9]\\d{9}$"))
}

/** 邮箱校验。 */
fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/** URL 校验。 */
fun String.isUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

/** 身份证校验。 */
fun String.isIdCard(): Boolean {
    return matches(Regex("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
}

/** MD5 加密。 */
fun String.md5(): String {
    val digest = java.security.MessageDigest.getInstance("MD5").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

/** SHA-256 加密。 */
fun String.sha256(): String {
    val digest = java.security.MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

/** 截断字符串。 */
fun String.ellipsize(maxLength: Int): String {
    return if (length > maxLength) substring(0, maxLength) + "..." else this
}

/** 手机号脱敏。 */
fun String.maskPhone(): String {
    return if (isPhone()) substring(0, 3) + "****" + substring(7) else this
}

/** 首字母大写。 */
fun String.capitalize(): String {
    return if (isNotEmpty()) this[0].uppercaseChar() + substring(1) else this
}

/** 首字母小写。 */
fun String.decapitalize(): String {
    return if (isNotEmpty()) this[0].lowercaseChar() + substring(1) else this
}

/** 判断是否为数字。 */
fun String.isNumber(): Boolean {
    return matches(Regex("^-?\\d+(\\.\\d+)?$"))
}

/** 判断是否为中文。 */
fun String.isChinese(): Boolean {
    return matches(Regex("^[\\u4e00-\\u9fa5]+$"))
}

/** 去除空格。 */
fun String.removeSpace(): String {
    return replace(" ", "")
}
