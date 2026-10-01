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
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.common.model

/**
 * 二元组。
 */
data class Tuple2<A, B>(val first: A, val second: B)

/**
 * 三元组。
 */
data class Tuple3<A, B, C>(val first: A, val second: B, val third: C)

/**
 * 四元组。
 */
data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

/**
 * 五元组。
 */
data class Tuple5<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
