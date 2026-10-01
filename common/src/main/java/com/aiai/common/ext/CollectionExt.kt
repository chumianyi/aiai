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

/**
 * 集合相关扩展函数集合。
 *
 * 提供安全获取、分页、分组、去重、排序、过滤、转换、统计等能力。
 */

// region 安全获取

/** 安全获取 List 元素，越界返回 null。 */
fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in indices) get(index) else null
}

/** 安全获取第一个元素，空列表返回 null。 */
fun <T> List<T>.safeFirst(): T? = if (isEmpty()) null else first()

/** 安全获取最后一个元素，空列表返回 null。 */
fun <T> List<T>.safeLast(): T? = if (isEmpty()) null else last()

/** 安全获取 Map 值，key 不存在返回 null。 */
fun <K, V> Map<K, V>.safeGet(key: K): V? = if (containsKey(key)) get(key) else null

// endregion

// region 去重

/** 按 [selector] 字段去重，保留首次出现的元素。 */
fun <T, K> List<T>.distinctBy(selector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { seen.add(selector(it)) }
}

/** 按 [selector] 字段去重，保留最后出现的元素。 */
fun <T, K> List<T>.distinctByLast(selector: (T) -> K): List<T> {
    val map = linkedMapOf<K, T>()
    forEach { map[selector(it)] = it }
    return map.values.toList()
}

// endregion

// region 分组 & 分页

/** 按 [keySelector] 分组。 */
fun <T, K> List<T>.groupBy(keySelector: (T) -> K): Map<K, List<T>> {
    val map = linkedMapOf<K, MutableList<T>>()
    forEach { item ->
        val key = keySelector(item)
        map.getOrPut(key) { mutableListOf() }.add(item)
    }
    return map
}

/** 分页：将列表按 [pageSize] 切分。 */
fun <T> List<T>.paginate(pageSize: Int): List<List<T>> {
    if (isEmpty()) return emptyList()
    val pages = mutableListOf<List<T>>()
    var i = 0
    while (i < size) {
        pages.add(subList(i, minOf(i + pageSize, size)))
        i += pageSize
    }
    return pages
}

/** 获取指定页的数据。 */
fun <T> List<T>.page(pageNum: Int, pageSize: Int): List<T> {
    val from = pageNum * pageSize
    if (from >= size) return emptyList()
    return subList(from, minOf(from + pageSize, size))
}

// endregion

// region 排序 & 过滤

/** 自然升序排序（可空安全）。 */
fun <T : Comparable<T>> List<T>?.orEmptySorted(): List<T> {
    return this?.sorted() ?: emptyList()
}

/** 按 [selector] 降序排序。 */
fun <T, R : Comparable<R>> List<T>.sortedByDescending(selector: (T) -> R): List<T> {
    return sortedWith(compareByDescending(selector))
}

/** 条件过滤：满足 [predicate] 时才过滤，否则原样返回。 */
fun <T> List<T>.filterIf(condition: Boolean, predicate: (T) -> Boolean): List<T> {
    return if (condition) filter(predicate) else this
}

/** 过滤 null 元素。 */
fun <T> List<T?>.filterNotNull(): List<T> {
    return filterNotNull()
}

// endregion

// region 转换 & 统计

/** 转换为 Map，[keySelector] 生成 key，[valueTransform] 生成 value。 */
fun <T, K, V> List<T>.associate(keySelector: (T) -> K, valueTransform: (T) -> V): Map<K, V> {
    val map = linkedMapOf<K, V>()
    forEach { map[keySelector(it)] = valueTransform(it) }
    return map
}

/** 扁平化嵌套列表。 */
fun <T> List<List<T>>.flatten(): List<T> {
    val result = mutableListOf<T>()
    forEach { result.addAll(it) }
    return result
}

/** 统计满足条件的元素个数。 */
fun <T> List<T>.countIf(predicate: (T) -> Boolean): Int {
    return count(predicate)
}

/** 求和（数字集合）。 */
fun List<Int>.sum(): Int {
    var s = 0
    forEach { s += it }
    return s
}

/** Double 集合求和。 */
fun List<Double>.sumDouble(): Double {
    var s = 0.0
    forEach { s += it }
    return s
}

/** 平均值。 */
fun List<Int>.averageDouble(): Double {
    return if (isEmpty()) 0.0 else sum().toDouble() / size
}

/** 最大值（可空安全）。 */
fun List<Int>.maxSafe(): Int? = if (isEmpty()) null else maxOrNull()

/** 最小值（可空安全）。 */
fun List<Int>.minSafe(): Int? = if (isEmpty()) null else minOrNull()

/** 拼接为字符串，带分隔符。 */
fun List<String>.join(separator: String): String = joinToString(separator)

/** 交集。 */
fun <T> Collection<T>.intersect(other: Collection<T>): Set<T> {
    return filter { it in other }.toSet()
}

/** 差集。 */
fun <T> Collection<T>.subtract(other: Collection<T>): Set<T> {
    return filter { it !in other }.toSet()
}

/** 并集。 */
fun <T> Collection<T>.union(other: Collection<T>): Set<T> {
    return (this + other).toSet()
}

/** 当列表为空时执行 [default] 提供默认列表。 */
fun <T> List<T>.ifEmpty(default: () -> List<T>): List<T> {
    return if (isEmpty()) default() else this
}

// endregion
