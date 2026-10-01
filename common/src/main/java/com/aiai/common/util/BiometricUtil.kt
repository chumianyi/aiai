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
package com.aiai.common.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * 生物识别工具类。
 *
 * 提供指纹/面容认证、密钥存储、认证回调等功能。
 *
 * 使用示例：
 * ```
 * BiometricUtil.authenticate(
 *     activity = activity,
 *     title = "指纹验证",
 *     subtitle = "请验证指纹",
 *     onSuccess = { ... },
 *     onError = { code, message -> ... }
 * )
 * ```
 */
object BiometricUtil {

    private const val KEYSTORE_NAME = "AndroidKeyStore"
    private const val DEFAULT_KEY_ALIAS = "aiai_biometric_key"
    private const val cipherTransform = "AES/CBC/PKCS7Padding"

    /**
     * 生物认证结果回调。
     */
    interface AuthCallback {
        /** 认证成功。 */
        fun onSuccess()

        /** 认证失败（用户取消或错误）。 */
        fun onError(errorCode: Int, errorMessage: CharSequence)

        /** 认证失败（指纹/面容不匹配）。 */
        fun onFailure()
    }

    /**
     * 检查设备是否支持生物识别。
     *
     * @param context 上下文
     * @return true 表示支持
     */
    fun isBiometricSupported(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    /**
     * 检查是否已录入生物信息。
     *
     * @param context 上下文
     * @return true 表示已录入
     */
    fun hasBiometricEnrolled(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }

    /**
     * 获取生物识别状态。
     *
     * @param context 上下文
     * @return 状态码
     */
    fun getBiometricStatus(context: Context): Int {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
    }

    /**
     * 进行生物识别认证。
     *
     * @param activity FragmentActivity
     * @param title 标题
     * @param subtitle 副标题
     * @param description 描述
     * @param negativeText 取消按钮文本
     * @param callback 认证回调
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String? = null,
        description: String? = null,
        negativeText: String = "取消",
        callback: AuthCallback
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callback.onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.onError(errorCode, errString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onFailure()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeText)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * 进行带加密的生物识别认证。
     *
     * @param activity FragmentActivity
     * @param keyAlias 密钥别名
     * @param title 标题
     * @param subtitle 副标题
     * @param description 描述
     * @param callback 认证回调
     */
    fun authenticateWithCrypto(
        activity: FragmentActivity,
        keyAlias: String = DEFAULT_KEY_ALIAS,
        title: String,
        subtitle: String? = null,
        description: String? = null,
        callback: AuthCallback
    ) {
        try {
            val secretKey = getOrCreateSecretKey(keyAlias)
            val cipher = Cipher.getInstance(cipherTransform)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val cryptoObject = BiometricPrompt.CryptoObject(cipher)

            val executor = ContextCompat.getMainExecutor(activity)
            val biometricPrompt = BiometricPrompt(activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        callback.onSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        callback.onError(errorCode, errString)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        callback.onFailure()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText("取消")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()

            biometricPrompt.authenticate(promptInfo, cryptoObject)
        } catch (e: Exception) {
            callback.onError(BiometricPrompt.ERROR_UNABLE_TO_PROCESS, e.message ?: "认证失败")
        }
    }

    /**
     * 获取或创建密钥。
     *
     * @param keyAlias 密钥别名
     * @return SecretKey
     */
    private fun getOrCreateSecretKey(keyAlias: String): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_NAME)
        keyStore.load(null)

        if (keyStore.containsAlias(keyAlias)) {
            return keyStore.getKey(keyAlias, null) as SecretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_NAME
        )

        val keyGenSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .build()

        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    /**
     * 删除密钥。
     *
     * @param keyAlias 密钥别名
     */
    fun deleteKey(keyAlias: String = DEFAULT_KEY_ALIAS) {
        val keyStore = KeyStore.getInstance(KEYSTORE_NAME)
        keyStore.load(null)
        keyStore.deleteEntry(keyAlias)
    }

    /**
     * 检查密钥是否存在。
     *
     * @param keyAlias 密钥别名
     * @return true 表示存在
     */
    fun hasKey(keyAlias: String = DEFAULT_KEY_ALIAS): Boolean {
        val keyStore = KeyStore.getInstance(KEYSTORE_NAME)
        keyStore.load(null)
        return keyStore.containsAlias(keyAlias)
    }

    /**
     * 错误码转可读文本。
     *
     * @param errorCode 错误码
     * @return 错误描述
     */
    fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            BiometricPrompt.ERROR_HW_UNAVAILABLE -> "硬件不可用"
            BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> "无法处理"
            BiometricPrompt.ERROR_TIMEOUT -> "操作超时"
            BiometricPrompt.ERROR_NO_SPACE -> "存储空间不足"
            BiometricPrompt.ERROR_CANCELED -> "操作已取消"
            BiometricPrompt.ERROR_LOCKOUT -> "尝试次数过多，请稍后再试"
            BiometricPrompt.ERROR_VENDOR -> "厂商错误"
            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> "已被永久锁定"
            BiometricPrompt.ERROR_USER_CANCELED -> "用户取消"
            BiometricPrompt.ERROR_NO_BIOMETRICS -> "未录入生物信息"
            BiometricPrompt.ERROR_HW_NOT_PRESENT -> "设备不支持生物识别"
            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> "用户点击取消"
            BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> "未设置屏幕锁"
            else -> "未知错误"
        }
    }
}
