/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * 语音消息View
 *
 * 显示语音消息的波形、播放进度和时长。
 * 点击播放/暂停，波形随进度动画。
 *
 * XML属性：
 * - voiceDuration: 语音时长（秒）
 * - waveColor: 波形颜色
 * - playedColor: 已播放部分颜色
 * - bubbleColor: 气泡背景色
 * - isFromUser: 是否用户发送
 */
class VoiceMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#E3F2FD")
    }

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#90CAF9")
    }

    private val playedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#2196F3")
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#666666")
        textSize = sp2px(12f)
    }

    private var voiceDuration: Long = 0L
    private var waveColor: Int = Color.parseColor("#90CAF9")
    private var playedColor: Int = Color.parseColor("#2196F3")
    private var isFromUser: Boolean = false
    private var maxBubbleWidth: Float = dp2px(160f)
    private var cornerRadius: Float = dp2px(20f)

    private var isPlaying: Boolean = false
    private var playProgress: Float = 0f
    private var playAnimator: ValueAnimator? = null
    private var waveformData: List<Int> = List(20) { (10..30).random() }

    private var onPlayClickListener: ((Long) -> Unit)? = null

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.VoiceMessageView, defStyleAttr, 0).apply {
            voiceDuration = getInt(com.aiai.chat.R.styleable.VoiceMessageView_voiceDuration, 0).toLong()
            isFromUser = getBoolean(com.aiai.chat.R.styleable.VoiceMessageView_isFromUser, false)
            recycle()
        }
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 根据时长动态计算宽度
        val progress = (voiceDuration / 60f).coerceIn(0.2f, 1f)
        val width = (dp2px(80f) + maxBubbleWidth * progress).toInt()
        val height = dp2px(40f).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // 绘制气泡背景
        val bubbleRect = RectF(0f, 0f, w, h)
        canvas.drawRoundRect(bubbleRect, cornerRadius, cornerRadius, bgPaint)

        // 绘制波形
        val waveCount = waveformData.size
        val waveAreaWidth = w - dp2px(60f)
        val waveBarWidth = dp2px(2f)
        val waveGap = (waveAreaWidth / waveCount) - waveBarWidth
        val centerY = h / 2f
        val maxHeight = h * 0.6f

        for (i in 0 until waveCount) {
            val x = dp2px(12f) + i * (waveBarWidth + waveGap)
            val amplitude = (waveformData[i] / 30f) * maxHeight
            val rect = RectF(x, centerY - amplitude / 2, x + waveBarWidth, centerY + amplitude / 2)

            // 根据进度判断是否已播放
            val waveProgress = i.toFloat() / waveCount
            val paint = if (waveProgress <= playProgress) playedPaint else wavePaint
            canvas.drawRoundRect(rect, waveBarWidth / 2, waveBarWidth / 2, paint)
        }

        // 绘制时长文字
        val durationText = "${voiceDuration}s"
        canvas.drawText(durationText, w - dp2px(40f), h / 2f + sp2px(4f), textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                togglePlay()
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun togglePlay() {
        isPlaying = !isPlaying
        if (isPlaying) {
            startPlayAnimation()
        } else {
            playAnimator?.cancel()
        }
        onPlayClickListener?.invoke(voiceDuration)
        invalidate()
    }

    private fun startPlayAnimation() {
        playAnimator?.cancel()
        playAnimator = ValueAnimator.ofFloat(playProgress, 1f).apply {
            duration = voiceDuration * 1000
            addUpdateListener { animation ->
                playProgress = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    /**
     * 设置语音时长
     */
    fun setDuration(durationSec: Long) {
        voiceDuration = durationSec
        requestLayout()
        invalidate()
    }

    /**
     * 设置波形数据
     */
    fun setWaveform(wave: List<Int>) {
        waveformData = wave
        invalidate()
    }

    /**
     * 设置播放进度（外部控制）
     */
    fun setPlayProgress(progress: Float) {
        playProgress = progress.coerceIn(0f, 1f)
        invalidate()
    }

    /**
     * 设置播放状态
     */
    fun setPlaying(playing: Boolean) {
        isPlaying = playing
        if (!playing) playAnimator?.cancel()
        invalidate()
    }

    /**
     * 设置播放点击监听
     */
    fun setOnPlayClickListener(listener: (Long) -> Unit) {
        onPlayClickListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        playAnimator?.cancel()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
}
