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
package com.aiai.chat.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aiai.chat.R
import com.aiai.chat.databinding.ActivityMainBinding
import com.aiai.chat.ui.fragment.ChatListFragment
import com.aiai.chat.ui.fragment.DiscoverFragment
import com.aiai.chat.ui.fragment.ProfileFragment
import com.aiai.chat.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 主界面Activity
 *
 * 包含底部导航（聊天/发现/我的），管理Fragment切换。
 * 负责权限申请和App初始化。
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val chatListFragment = ChatListFragment.newInstance()
    private val discoverFragment = DiscoverFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    private var activeFragment: Fragment = chatListFragment

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.all { it.value }
        viewModel.onPermissionResult(allGranted)
        if (!allGranted) {
            Toast.makeText(this, "部分权限被拒绝，部分功能可能无法使用", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[MainViewModel::class.java]

        initFragments()
        initBottomNavigation()
        requestPermissionsIfNeeded()
        initApp()
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, profileFragment, "profile").hide(profileFragment)
            add(R.id.fragment_container, discoverFragment, "discover").hide(discoverFragment)
            add(R.id.fragment_container, chatListFragment, "chat")
        }.commit()
    }

    private fun initBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> {
                    switchFragment(chatListFragment)
                    true
                }
                R.id.nav_discover -> {
                    switchFragment(discoverFragment)
                    true
                }
                R.id.nav_profile -> {
                    switchFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(target: Fragment) {
        if (target === activeFragment) return
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(target)
            .commit()
        activeFragment = target
    }

    private fun requestPermissionsIfNeeded() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO)
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        } else {
            viewModel.onPermissionResult(true)
        }
    }

    private fun initApp() {
        lifecycleScope.launch {
            // 初始化各项服务
            viewModel.onInitialized()
        }
    }

    override fun onBackPressed() {
        // 双击退出
        if (System.currentTimeMillis() - lastBackPressedTime > 2000) {
            lastBackPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "再按一次退出爱Ai", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private var lastBackPressedTime = 0L
    }
}
