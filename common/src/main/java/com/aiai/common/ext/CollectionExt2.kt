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

/**
 * 集合扩展函数集（扩展版）。
 */

/** 安全获取元素。 */
fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in 0 until size) this[index] else null
}

/** 第一个元素（可空）。 */
fun <T> List<T>.firstOrNull(): T? {
    return if (isEmpty()) null else this[0]
}

/** 最后一个元素（可空）。 */
fun <T> List<T>.lastOrNull(): T? {
    return if (isEmpty()) null else this[size - 1]
}

/** 去重（按字段）。 */
fun <T, K> List<T>.distinctByKey(selector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { seen.add(selector(it)) }
}

/** 分组。 */
fun <T, K> List<T>.groupByKey(selector: (T) -> K): Map<K, List<T>> {
    val map = mutableMapOf<K, MutableList<T>>()
    forEach { item ->
        val key = selector(item)
        map.getOrPut(key) { mutableListOf() }.add(item)
    }
    return map
}

/** 分页。 */
fun <T> List<T>.paginate(pageSize: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var i = 0
    while (i < size) {
        result.add(subList(i, minOf(i + pageSize, size)))
        i += pageSize
    }
    return result
}

/** 求和。 */
fun List<Int>.sum(): Int {
    var s = 0
    forEach { s += it }
    return s
}

/** 平均值。 */
fun List<Int>.average(): Double {
    return if (isEmpty()) 0.0 else sum().toDouble() / size
}

/** 最大值。 */
fun List<Int>.maxSafe(): Int? {
    return if (isEmpty()) null else maxOrNull()
}

/** 最小值。 */
fun List<Int>.minSafe(): Int? {
    return if (isEmpty()) null else minOrNull()
}

/** Map 安全获取。 */
fun <K, V> Map<K, V>.safeGet(key: K): V? {
    return this[key]
}

/** Map 转 List。 */
fun <K, V> Map<K, V>.toList(): List<Pair<K, V>> {
    return map { it.key to it.value }
}
