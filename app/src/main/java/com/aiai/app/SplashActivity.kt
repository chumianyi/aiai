/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aiai.app.databinding.ActivitySplashBinding
import com.aiai.core.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页Activity
 *
 * 职责：
 * - 展示品牌启动画面
 * - 检查深链接
 * - 判断是否首次启动（引导页）
 * - 跳转到主界面
 */
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
        private const val SPLASH_DELAY_MS = 1200L
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LogUtil.d(TAG, "onCreate")

        // 进入动画
        binding.ivLogo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).start()
        binding.tvAppName.animate().alpha(1f).translationY(0f).setStartDelay(300).setDuration(500).start()

        lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            navigateToNext()
        }
    }

    /**
     * 根据启动状态决定下一个页面
     */
    private fun navigateToNext() {
        val deepLink = handleDeepLink(intent)
        if (deepLink != null) {
            startActivity(deepLink)
            finish()
            return
        }

        val intent = Intent(this, AppEntryActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun handleDeepLink(intent: Intent?): Intent? {
        return if (intent?.data != null) {
            DeepLinkHandler.parseIntent(intent)
        } else {
            null
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
