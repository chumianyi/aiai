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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.enums.AttachmentType
import com.aiai.chat.ui.adapter.AttachmentAdapter

/**
 * 附件选择面板Fragment
 *
 * 底部弹出的附件选择面板，提供图片、文件、拍照、语音等选项。
 */
class AttachmentPanelFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AttachmentAdapter

    var onAttachmentSelectedListener: ((AttachmentType) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_attachment_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_attachment_options)
        adapter = AttachmentAdapter { type ->
            onAttachmentSelectedListener?.invoke(type)
            dismiss()
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.adapter = adapter
    }

    /**
     * 关闭面板
     */
    fun dismiss() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    companion object {
        fun newInstance(): AttachmentPanelFragment {
            return AttachmentPanelFragment()
        }
    }
}
