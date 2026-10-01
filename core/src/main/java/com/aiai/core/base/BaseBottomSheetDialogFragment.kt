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
package com.aiai.core.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 底部弹窗基类。
 *
 * 功能：
 * - ViewBinding 封装
 * - 圆角背景
 * - 拖拽手势
 * - 高度控制
 * - 点击外部关闭
 */
abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: VB
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), getTheme())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        setupBottomSheet()
    }

    /** 子类提供 ViewBinding 实例。 */
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /** 初始化 View。 */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 获取弹窗主题样式。
     *
     * @return 样式资源ID
     */
    protected open fun getTheme(): Int {
        return com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog
    }

    /** 设置底部弹窗属性。 */
    protected open fun setupBottomSheet() {
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let { sheet ->
                val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(sheet)
                behavior.setBottomSheetCallback(object :
                    com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        onSheetStateChanged(newState)
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        onSheetSlide(slideOffset)
                    }
                })

                // 设置初始状态为展开
                behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED

                // 设置最大高度
                setupMaxHeight(sheet)
            }
        }
    }

    /**
     * 设置最大高度。
     *
     * @param bottomSheet 底部 View
     */
    protected open fun setupMaxHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        val screenHeight = resources.displayMetrics.heightPixels
        layoutParams.height = (screenHeight * getMaxHeightRatio()).toInt()
        bottomSheet.layoutParams = layoutParams
    }

    /**
     * 获取最大高度比例。
     *
     * @return 高度比例（0-1）
     */
    protected open fun getMaxHeightRatio(): Float = 0.7f

    /**
     * 弹窗状态变化回调。
     *
     * @param newState 新状态
     */
    protected open fun onSheetStateChanged(newState: Int) {}

    /**
     * 弹窗滑动回调。
     *
     * @param slideOffset 滑动偏移
     */
    protected open fun onSheetSlide(slideOffset: Float) {}

    /** 关闭弹窗。 */
    fun dismissDialog() {
        dismiss()
    }

    /** 是否可取消。 */
    var cancelableOnTouchOutside: Boolean = true
        set(value) {
            field = value
            isCancelable = value
        }
}
