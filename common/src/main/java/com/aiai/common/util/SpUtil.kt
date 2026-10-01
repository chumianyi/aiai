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
import android.content.SharedPreferences

/**
 * SharedPreferences 工具类。
 *
 * 提供键值对存储、批量操作、监听等功能。
 */
object SpUtil {

    private const val DEFAULT_SP_NAME = "aiai_sp"

    private var sp: SharedPreferences? = null

    /**
     * 初始化。
     *
     * @param context 上下文
     * @param spName SP 名称
     */
    fun init(context: Context, spName: String = DEFAULT_SP_NAME) {
        if (sp == null) {
            sp = context.applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        }
    }

    /**
     * 保存字符串。
     *
     * @param key 键
     * @param value 值
     */
    fun putString(key: String, value: String) {
        sp?.edit()?.putString(key, value)?.apply()
    }

    /**
     * 获取字符串。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 字符串值
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return sp?.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存布尔值。
     *
     * @param key 键
     * @param value 值
     */
    fun putBoolean(key: String, value: Boolean) {
        sp?.edit()?.putBoolean(key, value)?.apply()
    }

    /**
     * 获取布尔值。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 布尔值
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sp?.getBoolean(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存整数。
     *
     * @param key 键
     * @param value 值
     */
    fun putInt(key: String, value: Int) {
        sp?.edit()?.putInt(key, value)?.apply()
    }

    /**
     * 获取整数。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 整数值
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sp?.getInt(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存长整数。
     *
     * @param key 键
     * @param value 值
     */
    fun putLong(key: String, value: Long) {
        sp?.edit()?.putLong(key, value)?.apply()
    }

    /**
     * 获取长整数。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 长整数值
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sp?.getLong(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存浮点值。
     *
     * @param key 键
     * @param value 值
     */
    fun putFloat(key: String, value: Float) {
        sp?.edit()?.putFloat(key, value)?.apply()
    }

    /**
     * 获取浮点值。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 浮点值
     */
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sp?.getFloat(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存字符串集合。
     *
     * @param key 键
     * @param values 值集合
     */
    fun putStringSet(key: String, values: Set<String>) {
        sp?.edit()?.putStringSet(key, values)?.apply()
    }

    /**
     * 获取字符串集合。
     *
     * @param key 键
     * @param defaultValue 默认值
     * @return 字符串集合
     */
    fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
        return sp?.getStringSet(key, defaultValue) ?: defaultValue
    }

    /**
     * 移除键值对。
     *
     * @param key 键
     */
    fun remove(key: String) {
        sp?.edit()?.remove(key)?.apply()
    }

    /**
     * 检查是否包含键。
     *
     * @param key 键
     * @return 是否包含
     */
    fun contains(key: String): Boolean {
        return sp?.contains(key) ?: false
    }

    /**
     * 清除所有数据。
     */
    fun clear() {
        sp?.edit()?.clear()?.apply()
    }

    /**
     * 获取所有键值对。
     *
     * @return 所有键值对
     */
    fun getAll(): Map<String, *> {
        return sp?.all ?: emptyMap<String, Any>()
    }

    /**
     * 同步保存。
     *
     * @param key 键
     * @param value 值
     * @return 是否成功
     */
    fun putStringSync(key: String, value: String): Boolean {
        return sp?.edit()?.putString(key, value)?.commit() ?: false
    }

    /**
     * 同步保存布尔值。
     *
     * @param key 键
     * @param value 值
     * @return 是否成功
     */
    fun putBooleanSync(key: String, value: Boolean): Boolean {
        return sp?.edit()?.putBoolean(key, value)?.commit() ?: false
    }

    /**
     * 同步保存整数。
     *
     * @param key 键
     * @param value 值
     * @return 是否成功
     */
    fun putIntSync(key: String, value: Int): Boolean {
        return sp?.edit()?.putInt(key, value)?.commit() ?: false
    }

    /**
     * 批量保存。
     *
     * @param action 编辑操作
     */
    fun batch(action: SharedPreferences.Editor.() -> Unit) {
        val editor = sp?.edit()
        editor?.action()
        editor?.apply()
    }
}
