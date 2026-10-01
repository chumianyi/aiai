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
package com.aiai.settings.manager

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Base64
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 密钥加密管理。
 *
 * 使用 Android Keystore 生成并保管 AES-256 主密钥（不可导出），
 * 对每条敏感配置做 AES/GCM/NoPadding 加密后写入普通 [SharedPreferences]。
 * 每条密文自带随机 12 字节 IV，格式为 Base64(IV || 密文+GCM标签)。
 *
 * 同时提供基于口令的 AES-GCM 加解密，用于密钥导出/导入文件。
 */
class KeyEncryptionManager(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(ENCRYPTED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }

    /** 读取 Keystore 中的主密钥，不存在则创建。 */
    private fun getOrCreateMasterKey(): SecretKey {
        (keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry)?.let {
            return it.secretKey
        }
        val generator = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE).apply {
            init(256)
        }
        return generator.generateKey().also {
            // Keystore 密钥自动持久化，无需手动保存
        }
    }

    /**
     * 保存明文 [plain] 到 [key]，自动用 Keystore 主密钥加密。
     */
    fun encryptAndStore(key: String, plain: String) {
        if (plain.isEmpty()) {
            prefs.edit().remove(key).apply()
            return
        }
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateMasterKey(), GCMParameterSpec(GCM_TAG_BITS, iv))
        val encrypted = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        val combined = ByteArray(iv.size + encrypted.size).also {
            System.arraycopy(iv, 0, it, 0, iv.size)
            System.arraycopy(encrypted, 0, it, iv.size, encrypted.size)
        }
        prefs.edit().putString(key, Base64.encodeToString(combined, Base64.NO_WRAP)).apply()
    }

    /**
     * 读取 [key] 对应明文，未保存返回 null；主密钥被系统清除时也返回 null。
     */
    fun decryptAndLoad(key: String): String? {
        val stored = prefs.getString(key, null) ?: return null
        return try {
            val combined = Base64.decode(stored, Base64.NO_WRAP)
            val iv = combined.copyOfRange(0, 12)
            val body = combined.copyOfRange(12, combined.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateMasterKey(), GCMParameterSpec(GCM_TAG_BITS, iv))
            String(cipher.doFinal(body), Charsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    /** 删除 [key]。 */
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    /** 清空所有加密存储（不删除 Keystore 主密钥）。 */
    fun clearAll() {
        prefs.edit().clear().apply()
    }

    // region 口令派生加解密（用于导入导出文件）

    /**
     * 用 [password] 派生密钥，加密 [data]，返回 Base64 字符串。
     */
    fun encryptWithPassword(data: String, password: String): String {
        val key = deriveKey(password)
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
        }
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val combined = ByteArray(iv.size + encrypted.size).also {
            System.arraycopy(iv, 0, it, 0, iv.size)
            System.arraycopy(encrypted, 0, it, iv.size, encrypted.size)
        }
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    /**
     * 逆向解密 [base64]，密码错误或损坏抛 [IllegalStateException]。
     */
    fun decryptWithPassword(base64: String, password: String): String {
        return try {
            val combined = Base64.decode(base64, Base64.NO_WRAP)
            val iv = combined.copyOfRange(0, 12)
            val body = combined.copyOfRange(12, combined.size)
            val key = deriveKey(password)
            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_BITS, iv))
            }
            String(cipher.doFinal(body), Charsets.UTF_8)
        } catch (e: Exception) {
            throw IllegalStateException("解密失败：密码错误或数据已损坏", e)
        }
    }

    /** SHA-256 派生出 256 位 AES 密钥。 */
    private fun deriveKey(password: String): SecretKeySpec {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        return SecretKeySpec(digest.digest(password.toByteArray(Charsets.UTF_8)), "AES")
    }

    // endregion

    companion object {
        private const val ENCRYPTED_PREFS_NAME = "aiai_secure_prefs"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "aiai_master_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_BITS = 128

        @Volatile
        private var instance: KeyEncryptionManager? = null

        /** 获取全局单例。 */
        fun get(context: Context): KeyEncryptionManager {
            return instance ?: synchronized(this) {
                instance ?: KeyEncryptionManager(context.applicationContext).also { instance = it }
            }
        }

        /**
         * 估算口令强度 0~4。
         */
        fun passwordStrength(pwd: String): Int {
            if (pwd.isEmpty()) return 0
            var score = 1
            if (pwd.length >= 6) score = 2
            if (pwd.length >= 8 && pwd.any { it.isDigit() }) score = 3
            if (pwd.length >= 10 && pwd.any { !it.isLetterOrDigit() }) score = 4
            return score
        }

        /** 是否支持 Keystore 主密钥（API 23+ 全部支持）。 */
        fun isKeystoreSupported(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}
