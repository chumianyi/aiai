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
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.aiai.common.ext.hideKeyboard
import com.aiai.common.util.other.Logger

/**
 * 所有 Activity 的基类。
 *
 * 功能：
 * - ViewBinding 封装
 * - Hilt 注入
 * - 沉浸式状态栏
 * - 生命周期日志
 * - 权限处理
 * - Loading 显示/隐藏
 * - 错误处理
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        // 沉浸式状态栏
        setupStatusBar()

        // 初始化 View
        initView(savedInstanceState)

        // 观察数据
        observeData()

        Logger.d("${javaClass.simpleName} onCreate")
    }

    /** 子类提供 ViewBinding 实例。 */
    abstract fun getViewBinding(): VB

    /** 初始化 View。 */
    abstract fun initView(savedInstanceState: Bundle?)

    /** 观察数据（可选重写）。 */
    open fun observeData() {}

    /** 设置状态栏。 */
    open fun setupStatusBar() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = android.graphics.Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    /** 显示 Loading。 */
    open fun showLoading() {}

    /** 隐藏 Loading。 */
    open fun hideLoading() {}

    /** 显示错误。 */
    open fun showError(message: String) {}

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
        Logger.d("${javaClass.simpleName} onDestroy")
    }
}
