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
package com.aiai.core.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger

/**
 * Activity 基类 V2。
 */
abstract class BaseActivity2<VB : ViewBinding> : androidx.appcompat.app.AppCompatActivity() {

    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        Logger.d("${javaClass.simpleName} onCreate")
        initView(savedInstanceState)
        initData()
    }

    abstract fun getViewBinding(): VB
    open fun initView(savedInstanceState: Bundle?) {}
    open fun initData() {}

    override fun onStart() { super.onStart(); Logger.d("${javaClass.simpleName} onStart") }
    override fun onResume() { super.onResume(); Logger.d("${javaClass.simpleName} onResume") }
    override fun onPause() { super.onPause(); Logger.d("${javaClass.simpleName} onPause") }
    override fun onStop() { super.onStop(); Logger.d("${javaClass.simpleName} onStop") }
    override fun onDestroy() { super.onDestroy(); Logger.d("${javaClass.simpleName} onDestroy") }
}
