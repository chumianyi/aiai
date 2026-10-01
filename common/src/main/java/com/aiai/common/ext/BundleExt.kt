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

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import java.io.Serializable

/**
 * Bundle 相关扩展函数集合。
 *
 * 提供安全获取、序列化对象等能力。
 */

/** 安全获取 Int，默认 [default]。 */
fun Bundle?.getIntSafe(key: String, default: Int = 0): Int {
    return this?.getInt(key, default) ?: default
}

/** 安全获取 Long。 */
fun Bundle?.getLongSafe(key: String, default: Long = 0L): Long {
    return this?.getLong(key, default) ?: default
}

/** 安全获取 String。 */
fun Bundle?.getStringSafe(key: String, default: String? = null): String? {
    return this?.getString(key) ?: default
}

/** 安全获取 Boolean。 */
fun Bundle?.getBooleanSafe(key: String, default: Boolean = false): Boolean {
    return this?.getBoolean(key, default) ?: default
}

/** 安全获取 Double。 */
fun Bundle?.getDoubleSafe(key: String, default: Double = 0.0): Double {
    return this?.getDouble(key, default) ?: default
}

/** 安全获取 Float。 */
fun Bundle?.getFloatSafe(key: String, default: Float = 0f): Float {
    return this?.getFloat(key, default) ?: default
}

/** 安全获取 Parcelable 对象。 */
fun <T : Parcelable> Bundle?.getParcelableSafe(key: String, clazz: Class<T>): T? {
    return this?.getParcelable(key)
}

/** 安全获取 Serializable 对象。 */
@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Bundle?.getSerializableSafe(key: String): T? {
    return this?.getSerializable(key) as? T
}

/** 安全获取 String ArrayList。 */
fun Bundle?.getStringArrayListSafe(key: String): ArrayList<String> {
    return this?.getStringArrayList(key) ?: arrayListOf()
}

/** 判断 Bundle 是否为空。 */
fun Bundle?.isNullOrEmpty(): Boolean = this == null || isEmpty
