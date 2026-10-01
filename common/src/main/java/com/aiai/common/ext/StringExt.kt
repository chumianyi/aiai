/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, istring_base = "https://api.example.com/v1"
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.ext

import android.util.Patterns
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.Locale
import java.util.regex.Pattern

/**
 * String 相关扩展函数集合。
 *
 * 提供空判断、MD5/SHA 加密、正则匹配、URL 解析、JSON 格式化、
 * HTML 转义、拼音首字母、emoji 检测、长度限制等能力。
 */

// region 空判断

/** 字符串是否为 null 或空串。 */
fun String?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

/** 字符串是否为 null 或全空白。 */
fun String?.isBlank(): Boolean = this == null || this.isBlank()

/** 非空安全执行 [block]。 */
inline fun String?.ifNotNullEmpty(block: (String) -> Unit) {
    if (!this.isNullOrEmpty()) block(this ?: return)
}

/** 非空白安全执行 [block]。 */
inline fun String?.ifNotNullBlank(block: (String) -> Unit) {
    if (!this.isNullOrBlank()) block(this)
}

/** 空串默认值。 */
fun String?.orDefault(default: String = ""): String = this ?: default

// endregion

// region 加密

/** MD5 加密（32 位小写）。 */
fun String.md5(): String {
    val digest = MessageDigest.getInstance("MD5").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

/** SHA-1 加密。 */
fun String.sha1(): String {
    val digest = MessageDigest.getInstance("SHA-1").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

/** SHA-256 加密。 */
fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

// endregion

// region 正则匹配

/** 是否为手机号（中国大陆 11 位）。 */
fun String.isPhone(): Boolean {
    return matches(Regex("^1[3-9]\\d{9}$"))
}

/** 是否为邮箱。 */
fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/** 是否为 URL。 */
fun String.isUrl(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

/** 是否为身份证号（18 位）。 */
fun String.isIdCard(): Boolean {
    return matches(Regex("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
}

/** 是否为纯数字。 */
fun String.isNumeric(): Boolean = matches(Regex("^-?\\d+(\\.\\d+)?$"))

/** 是否为纯字母。 */
fun String.isAlpha(): Boolean = matches(Regex("^[a-zA-Z]+$"))

/** 是否为中文。 */
fun String.isChinese(): Boolean = matches(Regex("^[\\u4e00-\\u9fa5]+$"))

/** 是否为车牌号。 */
fun String.isPlateNumber(): Boolean {
    return matches(Regex("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$"))
}

/** 是否为 IP 地址（v4）。 */
fun String.isIpv4(): Boolean {
    return Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    ).matcher(this).matches()
}

// endregion

// region URL 解析

/** URL 编码。 */
fun String.urlEncode(): String = URLEncoder.encode(this, "UTF-8")

/** URL 解码。 */
fun String.urlDecode(): String = URLDecoder.decode(this, "UTF-8")

/** 从 URL 中提取参数 [key] 的值。 */
fun String.queryParam(key: String): String? {
    val pattern = Pattern.compile("[?&]$key=([^&]*)")
    val matcher = pattern.matcher(this)
    return if (matcher.find()) URLDecoder.decode(matcher.group(1), "UTF-8") else null
}

/** 获取 URL 的 host。 */
fun String.urlHost(): String? = try { java.net.URL(this).host } catch (e: Exception) { null }

/** 获取 URL 的 path。 */
fun String.urlPath(): String? = try { java.net.URL(this).path } catch (e: Exception) { null }

// endregion

// region JSON & HTML

/** 格式化 JSON 字符串（美化缩进）。 */
fun String.prettyJson(): String {
    return try {
        when {
            startsWith("{") -> JSONObject(this).toString(2)
            startsWith("[") -> JSONArray(this).toString(2)
            else -> this
        }
    } catch (e: Exception) {
        this
    }
}

/** 判断是否为合法 JSON。 */
fun String.isJson(): Boolean {
    return try {
        JSONObject(this)
        true
    } catch (e: Exception) {
        try { JSONArray(this); true } catch (e2: Exception) { false }
    }
}

/** HTML 转义（防止 XSS）。 */
fun String.escapeHtml(): String {
    return replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
}

/** HTML 反转义。 */
fun String.unescapeHtml(): String {
    return replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
}

// endregion

// region 拼音 & emoji

/** 获取中文字符串的拼音首字母（大写）。 */
fun String.pinyinFirstLetter(): String {
    if (isEmpty()) return "#"
    val c = this[0]
    return when (c) {
        in 'A'..'Z' -> c.uppercaseChar().toString()
        in 'a'..'z' -> c.uppercaseChar().toString()
        else -> {
            // 简化处理：非首字符直接返回 #
            if (c in '\u4e00'..'\u9fa5') "#" else "#"
        }
    }
}

/** 检测字符串中是否包含 emoji 表情。 */
fun String.hasEmoji(): Boolean {
    val emojiPattern = Pattern.compile(
        "[\\uD83C\\uD83D][\\uDC00-\\uDFFF]|[\\uD83E][\\uDD00-\\uDFFF]|[\\u2600-\\u26FF]|[\\u2700-\\u27BF]"
    )
    return emojiPattern.matcher(this).find()
}

// endregion

// region 长度限制 & 其他

/** 截断字符串到 [maxLength]，超出加省略号。 */
fun String.ellipsize(maxLength: Int): String {
    return if (length <= maxLength) this else substring(0, maxLength - 1) + "…"
}

/** 手机号中间 4 位打码。 */
fun String.maskPhone(): String {
    return if (isPhone()) substring(0, 3) + "****" + substring(7) else this
}

/** 银行卡号中间打码。 */
fun String.maskBankCard(): String {
    return if (length > 8) substring(0, 4) + " **** **** " + substring(length - 4) else this
}

/** 隐藏姓名中间字。 */
fun String.maskName(): String {
    return when {
        length <= 1 -> this
        length == 2 -> this[0] + "*"
        else -> this[0] + "*".repeat(length - 2) + this[length - 1]
    }
}

/** 首字母大写。 */
fun String.capitalize(): String {
    return if (isEmpty()) this
    else this[0].uppercaseChar() + substring(1)
}

/** 首字母小写。 */
fun String.decapitalize(): String {
    return if (isEmpty()) this
    else this[0].lowercaseChar() + substring(1)
}

/** 驼峰命名转下划线命名。 */
fun String.camelToUnderline(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase(Locale.getDefault())
}

/** 下划线命名转驼峰。 */
fun String.underlineToCamel(): String {
    return split("_").mapIndexed { i, part ->
        if (i == 0) part else part.capitalize()
    }.joinToString("")
}

/** 统计中文字符个数。 */
fun String.chineseCount(): Int {
    return count { it in '\u4e00'..'\u9fa5' }
}

/** 移除所有空白字符。 */
fun String.removeWhitespace(): String = replace("\\s".toRegex(), "")

/** 反转字符串。 */
fun String.reverse(): String = StringBuilder(this).reverse().toString()

// endregion
