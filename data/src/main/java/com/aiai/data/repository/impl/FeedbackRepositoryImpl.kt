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
package com.aiai.data.repository.impl

import android.util.Log
import com.aiai.data.model.Feedback
import com.aiai.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 反馈仓库实现类。
 *
 * 提供用户反馈提交、查询、回复等功能。
 */
class FeedbackRepositoryImpl : FeedbackRepository {

    companion object {
        private const val TAG = "FeedbackRepositoryImpl"
    }

    private val feedbackList = mutableListOf<Feedback>()

    override suspend fun submitFeedback(
        type: String,
        title: String,
        content: String,
        contact: String?
    ): Result<Feedback> {
        Log.d(TAG, "Submit feedback: $type, $title")
        return try {
            if (title.isBlank() || content.isBlank()) {
                Result.failure(Exception("标题和内容不能为空"))
            } else {
                val feedback = Feedback(
                    id = "fb_${System.currentTimeMillis()}",
                    type = type,
                    title = title,
                    content = content,
                    contact = contact,
                    status = "pending",
                    createdAt = System.currentTimeMillis(),
                    reply = null
                )
                feedbackList.add(0, feedback)
                Log.d(TAG, "Feedback submitted: ${feedback.id}")
                Result.success(feedback)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Submit feedback failed", e)
            Result.failure(e)
        }
    }

    override fun getFeedbackList(): Flow<List<Feedback>> = flow {
        emit(feedbackList.toList())
    }

    override suspend fun getFeedbackById(id: String): Feedback? {
        return feedbackList.find { it.id == id }
    }

    override suspend fun deleteFeedback(id: String): Result<Unit> {
        Log.d(TAG, "Delete feedback: $id")
        return try {
            val removed = feedbackList.removeIf { it.id == id }
            if (removed) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("反馈不存在"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Delete feedback failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getFeedbackCount(): Int {
        return feedbackList.size
    }
}
