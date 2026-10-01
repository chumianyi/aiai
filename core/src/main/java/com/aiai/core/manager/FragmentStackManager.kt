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
package com.aiai.core.manager

import androidx.fragment.app.Fragment

/**
 * Fragment 栈管理器。
 *
 * 管理 Fragment 的添加、移除、回退。
 */
object FragmentStackManager {

    private val stack = mutableListOf<Fragment>()

    /** 添加 Fragment。 */
    fun push(fragment: Fragment) {
        stack.add(fragment)
    }

    /** 移除顶部 Fragment。 */
    fun pop(): Fragment? {
        return if (stack.isNotEmpty()) stack.removeAt(stack.size - 1) else null
    }

    /** 移除所有 Fragment。 */
    fun clear() {
        stack.clear()
    }

    /** 当前 Fragment 数量。 */
    fun size(): Int = stack.size

    /** 是否为空。 */
    fun isEmpty(): Boolean = stack.isEmpty()

    /** 获取栈顶 Fragment。 */
    fun top(): Fragment? = stack.lastOrNull()
}
