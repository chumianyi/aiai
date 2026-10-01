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

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences 委托属性扩展。
 *
 * 通过属性委托方式简化 SP 读写，支持 String/Int/Long/Float/Boolean/StringSet。
 */

/** String 类型委托。 */
fun SharedPreferences.string(key: String, defaultValue: String = ""): ReadWriteProperty<Any?, String> {
    return object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            getString(key, defaultValue) ?: defaultValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            edit().putString(key, value).apply()
        }
    }
}

/** Int 类型委托。 */
fun SharedPreferences.int(key: String, defaultValue: Int = 0): ReadWriteProperty<Any?, Int> {
    return object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = getInt(key, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            edit().putInt(key, value).apply()
        }
    }
}

/** Long 类型委托。 */
fun SharedPreferences.long(key: String, defaultValue: Long = 0L): ReadWriteProperty<Any?, Long> {
    return object : ReadWriteProperty<Any?, Long> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Long = getLong(key, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            edit().putLong(key, value).apply()
        }
    }
}

/** Float 类型委托。 */
fun SharedPreferences.float(key: String, defaultValue: Float = 0f): ReadWriteProperty<Any?, Float> {
    return object : ReadWriteProperty<Any?, Float> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Float = getFloat(key, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            edit().putFloat(key, value).apply()
        }
    }
}

/** Boolean 类型委托。 */
fun SharedPreferences.boolean(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any?, Boolean> {
    return object : ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = getBoolean(key, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            edit().putBoolean(key, value).apply()
        }
    }
}

/** StringSet 类型委托。 */
fun SharedPreferences.stringSet(
    key: String,
    defaultValue: Set<String> = emptySet()
): ReadWriteProperty<Any?, Set<String>> {
    return object : ReadWriteProperty<Any?, Set<String>> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> =
            getStringSet(key, defaultValue) ?: defaultValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) {
            edit().putStringSet(key, value).apply()
        }
    }
}

/** 同步提交（commit）版本 String 委托。 */
fun SharedPreferences.stringSync(key: String, defaultValue: String = ""): ReadWriteProperty<Any?, String> {
    return object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            getString(key, defaultValue) ?: defaultValue
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            edit().putString(key, value).commit()
        }
    }
}

/** 同步提交 Boolean 委托。 */
fun SharedPreferences.booleanSync(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any?, Boolean> {
    return object : ReadWriteProperty<Any?, Boolean> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = getBoolean(key, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            edit().putBoolean(key, value).commit()
        }
    }
}

/** 移除指定 key。 */
fun SharedPreferences.remove(key: String) {
    edit().remove(key).apply()
}

/** 清空所有数据。 */
fun SharedPreferences.clear() {
    edit().clear().apply()
}

/** 判断是否包含 key。 */
fun SharedPreferences.contains(key: String): Boolean = contains(key)
