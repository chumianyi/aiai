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

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Button 扩展函数集合。
 *
 * 提供加载状态、倒计时、防抖点击、样式切换等常用 Button 操作。
 */

// region 加载状态

/**
 * 显示加载状态。
 *
 * @param loadingText 加载中文本
 * @param originalText 原始文本（保存以便恢复）
 */
fun Button.showLoading(
    loadingText: String = "加载中...",
    originalText: String = text.toString()
) {
    isEnabled = false
    tag = originalText
    text = loadingText
    alpha = 0.7f
}

/**
 * 隐藏加载状态。
 *
 * @param resultText 结果文本，默认恢复原始文本
 */
fun Button.hideLoading(resultText: String? = null) {
    isEnabled = true
    alpha = 1f
    val original = tag as? String
    text = resultText ?: original ?: text
}

/**
 * 是否处于加载状态。
 */
fun Button.isLoading(): Boolean = !isEnabled

// endregion

// region 倒计时

/**
 * 开始倒计时。
 *
 * @param totalTime 总时长（毫秒）
 * @param interval 间隔（毫秒）
 * @param onTick 每次 tick 回调
 * @param onFinish 倒计时结束回调
 */
fun Button.startCountDown(
    totalTime: Long = 60000L,
    interval: Long = 1000L,
    onTick: (Long) -> Unit = { millisUntilFinished ->
        text = "${millisUntilFinished / 1000}s 后重试"
    },
    onFinish: () -> Unit = {
        isEnabled = true
        text = "获取验证码"
    }
) {
    isEnabled = false
    object : CountDownTimer(totalTime, interval) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            onFinish()
        }
    }.start()
}

/**
 * 验证码倒计时。
 *
 * @param seconds 倒计时秒数
 */
fun Button.startSmsCountDown(seconds: Int = 60) {
    startCountDown(
        totalTime = seconds * 1000L,
        onTick = { millisUntilFinished ->
            text = "${millisUntilFinished / 1000}s 后重发"
        },
        onFinish = {
            isEnabled = true
            text = "重新获取"
        }
    )
}

// endregion

// region 防抖点击

/**
 * 防抖点击。
 *
 * @param interval 防抖间隔（毫秒）
 * @param action 点击动作
 */
fun Button.onClickDebounce(
    interval: Long = 500L,
    action: () -> Unit
) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > interval) {
            lastClickTime = currentTime
            action()
        }
    }
}

/**
 * 防双击。
 *
 * @param interval 间隔（毫秒）
 * @param action 点击动作
 */
fun Button.onSingleClick(
    interval: Long = 1000L,
    action: () -> Unit
) {
    onClickDebounce(interval, action)
}

// endregion

// region 样式切换

/**
 * 设置主按钮样式。
 *
 * @param cornerRadius 圆角半径
 * @param backgroundColor 背景色
 * @param textColor 文本颜色
 */
fun Button.setPrimaryStyle(
    cornerRadius: Float = 8f,
    @ColorInt backgroundColor: Int = Color.parseColor("#2196F3"),
    @ColorInt textColor: Int = Color.WHITE
) {
    background = GradientDrawable().apply {
        setColor(backgroundColor)
        cornerRadius = cornerRadius
    }
    setTextColor(textColor)
}

/**
 * 设置描边按钮样式。
 *
 * @param cornerRadius 圆角半径
 * @param borderColor 边框颜色
 * @param borderWidth 边框宽度
 * @param textColor 文本颜色
 */
fun Button.setOutlineStyle(
    cornerRadius: Float = 8f,
    @ColorInt borderColor: Int = Color.parseColor("#2196F3"),
    borderWidth: Float = 2f,
    @ColorInt textColor: Int = Color.parseColor("#2196F3")
) {
    background = GradientDrawable().apply {
        setStroke(borderWidth.toInt(), borderColor)
        cornerRadius = cornerRadius
    }
    setTextColor(textColor)
}

/**
 * 设置危险按钮样式。
 *
 * @param cornerRadius 圆角半径
 */
fun Button.setDangerousStyle(cornerRadius: Float = 8f) {
    setPrimaryStyle(cornerRadius, Color.parseColor("#F44336"))
}

/**
 * 设置成功按钮样式。
 *
 * @param cornerRadius 圆角半径
 */
fun Button.setSuccessStyle(cornerRadius: Float = 8f) {
    setPrimaryStyle(cornerRadius, Color.parseColor("#4CAF50"))
}

/**
 * 设置禁用样式。
 */
fun Button.setDisabledStyle() {
    alpha = 0.5f
    isEnabled = false
}

/**
 * 恢复启用样式。
 */
fun Button.setEnabledStyle() {
    alpha = 1f
    isEnabled = true
}

// endregion

// region 其他

/**
 * 设置按钮文本并从资源加载。
 *
 * @param resId 字符串资源ID
 */
fun Button.setTextCompat(@StringRes resId: Int) {
    setText(resId)
}

/**
 * 按钮按下缩放效果。
 *
 * @param scale 缩放比例
 * @param duration 动画时长
 */
fun Button.setPressScaleEffect(scale: Float = 0.95f, duration: Long = 100) {
    setOnTouchListener { v, event ->
        when (event.action) {
            android.view.MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
            }
            android.view.MotionEvent.ACTION_UP,
            android.view.MotionEvent.ACTION_CANCEL -> {
                v.animate().scaleX(1f).scaleY(1f).setDuration(duration).start()
            }
        }
        false
    }
}

/**
 * 按钮点击波纹效果。
 *
 * @param color 波纹颜色
 */
fun Button.setRippleEffect(@ColorInt color: Int = Color.parseColor("#22000000")) {
    // 简化实现，实际可通过 RippleDrawable 实现
}

/**
 * 禁用按钮并设置透明度。
 *
 * @param alpha 透明度
 */
fun Button.disableWithAlpha(alpha: Float = 0.5f) {
    isEnabled = false
    this.alpha = alpha
}

/**
 * 启用按钮并恢复透明度。
 */
fun Button.enableWithAlpha() {
    isEnabled = true
    this.alpha = 1f
}

/**
 * 按钮状态切换（选中/未选中）。
 *
 * @param selected 是否选中
 * @param selectedColor 选中颜色
 * @param unselectedColor 未选中颜色
 */
fun Button.toggleSelected(
    selected: Boolean,
    @ColorInt selectedColor: Int = Color.parseColor("#2196F3"),
    @ColorInt unselectedColor: Int = Color.parseColor("#E0E0E0")
) {
    isSelected = selected
    background = GradientDrawable().apply {
        setColor(if (selected) selectedColor else unselectedColor)
        cornerRadius = 8f
    }
}

/**
 * 切换按钮选中状态。
 */
fun Button.toggle() {
    isSelected = !isSelected
}

/**
 * 按钮带图标。
 *
 * @param drawableRes 图标资源
 * @param position 图标位置（左/右/上/下）
 * @param size 图标尺寸
 */
fun Button.setIcon(
    drawableRes: Int,
    position: IconPosition = IconPosition.LEFT,
    size: Int = 48
) {
    val drawable = context.getDrawable(drawableRes) ?: return
    drawable.setBounds(0, 0, size, size)
    when (position) {
        IconPosition.LEFT -> setCompoundDrawables(drawable, null, null, null)
        IconPosition.RIGHT -> setCompoundDrawables(null, null, drawable, null)
        IconPosition.TOP -> setCompoundDrawables(null, drawable, null, null)
        IconPosition.BOTTOM -> setCompoundDrawables(null, null, null, drawable)
    }
}

/** 图标位置枚举。 */
enum class IconPosition {
    LEFT, RIGHT, TOP, BOTTOM
}

// endregion
