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
package com.aiai.common.util.security

import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64

/**
 * 应用签名工具类。
 *
 * 获取 App 的签名信息，用于安全校验。
 */
object SignatureUtil {

    /** 获取应用签名的 MD5 指纹。 */
    fun getSignatureMd5(context: Context): String? {
        return try {
            val pm = context.packageManager
            @Suppress("DEPRECATION")
            val info = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val sig = info.signatures?.get(0)?.toByteArray() ?: return null
            val md = java.security.MessageDigest.getInstance("MD5")
            md.digest(sig).joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            null
        }
    }

    /** 获取应用签名的 Base64 编码。 */
    fun getSignatureBase64(context: Context): String? {
        return try {
            val pm = context.packageManager
            @Suppress("DEPRECATION")
            val info = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val sig = info.signatures?.get(0)?.toByteArray() ?: return null
            Base64.encodeToString(sig, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }
}
