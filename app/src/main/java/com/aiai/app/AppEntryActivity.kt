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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aiai.app.databinding.ActivityAppEntryBinding
import com.aiai.chat.ui.ChatFragment
import com.aiai.settings.ui.SettingsFragment
import com.aiai.core.util.LogUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * 应用入口Activity（主容器）
 *
 * 包含底部导航，切换主要功能Fragment：
 * - 对话（Chat）
 * - 历史（History）
 * - 设置（Settings）
 */
@AndroidEntryPoint
class AppEntryActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AppEntryActivity"
        private const val EXTRA_INITIAL_TAB = "extra_initial_tab"
        private const val TAB_CHAT = 0
        private const val TAB_HISTORY = 1
        private const val TAB_SETTINGS = 2
    }

    private lateinit var binding: ActivityAppEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LogUtil.d(TAG, "onCreate")

        setupBottomNavigation()
        setupInitialFragment(savedInstanceState, intent)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> { switchTab(TAB_CHAT); true }
                R.id.nav_history -> { switchTab(TAB_HISTORY); true }
                R.id.nav_settings -> { switchTab(TAB_SETTINGS); true }
                else -> false
            }
        }
    }

    private fun setupInitialFragment(savedInstanceState: Bundle?, intent: Intent?) {
        val tab = intent?.getIntExtra(EXTRA_INITIAL_TAB, TAB_CHAT) ?: TAB_CHAT
        if (savedInstanceState == null) {
            switchTab(tab)
        }
    }

    private fun switchTab(tab: Int) {
        val fragment: Fragment = when (tab) {
            TAB_CHAT -> ChatFragment.newInstance()
            TAB_SETTINGS -> SettingsFragment.newInstance()
            else -> ChatFragment.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNowAllowingStateLoss()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
