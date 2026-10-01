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
package com.aiai.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aiai.chat.R
import com.aiai.chat.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

/**
 * 个人中心Fragment
 *
 * 展示用户信息、设置入口、使用统计、关于。
 */
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var tvNickname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvMembership: TextView
    private lateinit var tvTotalChats: TextView
    private lateinit var tvTotalMessages: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        tvNickname = view.findViewById(R.id.tv_nickname)
        tvEmail = view.findViewById(R.id.tv_email)
        tvMembership = view.findViewById(R.id.tv_membership)
        tvTotalChats = view.findViewById(R.id.tv_total_chats)
        tvTotalMessages = view.findViewById(R.id.tv_total_messages)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userInfo.collect { info ->
                tvNickname.text = info.nickname
                tvEmail.text = info.email
                tvMembership.text = info.membershipType
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usageStats.collect { stats ->
                tvTotalChats.text = "${stats.totalChats}"
                tvTotalMessages.text = "${stats.totalMessages}"
            }
        }
    }

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
