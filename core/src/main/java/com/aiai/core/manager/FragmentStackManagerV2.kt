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
import androidx.fragment.app.FragmentActivity

/**
 * Fragment 栈管理器 V2。
 */
object FragmentStackManagerV2 {
    private val stack = mutableListOf<Fragment>()

    fun push(f: Fragment) { stack.add(f) }
    fun pop() { if (stack.isNotEmpty()) stack.removeAt(stack.size - 1) }
    fun size(): Int = stack.size
    fun clear() { stack.clear() }
}
