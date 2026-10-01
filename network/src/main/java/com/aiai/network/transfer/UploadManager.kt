/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.transfer

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 上传管理器，管理文件上传任务。
 *
 * 功能：
 * - 单文件/多文件上传
 * - 进度回调
 * - 取消上传
 * - 任务状态管理
 */
class UploadManager(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build(),
) {

    companion object {
        private const val TAG = "UploadManager"
        private const val DEFAULT_MIME_TYPE = "application/octet-stream"
    }

    private val tasks = ConcurrentHashMap<String, TransferTask>()
    private val calls = ConcurrentHashMap<String, Call>()
    private val listeners = ConcurrentHashMap<String, TransferListener>()

    /**
     * 上传文件。
     *
     * @param url 上传URL
     * @param file 要上传的文件
     * @param mimeType 文件MIME类型
     * @param listener 传输监听器
     * @return 任务ID
     */
    fun uploadFile(
        url: String,
        file: File,
        mimeType: String = DEFAULT_MIME_TYPE,
        listener: TransferListener? = null,
    ): String {
        val taskId = "upload_${System.currentTimeMillis()}_${file.name.hashCode()}"

        val task = TransferTask(
            taskId = taskId,
            url = url,
            filePath = file.absolutePath,
            totalBytes = file.length(),
            status = TransferStatus.PENDING,
            type = TransferType.UPLOAD,
        )
        tasks[taskId] = task
        listener?.let { listeners[taskId] = it }
        listener?.onStart(task)

        val requestBody = file.asRequestBody(mimeType.toMediaType())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, requestBody)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(multipartBody)
            .build()

        val call = client.newCall(request)
        calls[taskId] = call

        tasks[taskId] = task.copy(status = TransferStatus.RUNNING)
        listeners[taskId]?.onProgress(task, 0, 0)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                tasks[taskId] = task.copy(status = TransferStatus.FAILED, error = e.message)
                listeners[taskId]?.onError(task, e.message ?: "Unknown error")
                cleanup(taskId)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    tasks[taskId] = task.copy(
                        status = TransferStatus.COMPLETED,
                        transferredBytes = file.length(),
                    )
                    listeners[taskId]?.onComplete(task, file.absolutePath)
                } else {
                    tasks[taskId] = task.copy(
                        status = TransferStatus.FAILED,
                        error = "HTTP ${response.code}",
                    )
                    listeners[taskId]?.onError(task, "HTTP ${response.code}: ${response.message}")
                }
                cleanup(taskId)
            }
        })

        return taskId
    }

    /**
     * 取消上传任务。
     *
     * @param taskId 任务ID
     */
    fun cancelUpload(taskId: String) {
        calls[taskId]?.cancel()
        tasks[taskId]?.let { task ->
            tasks[taskId] = task.copy(status = TransferStatus.CANCELLED)
            listeners[taskId]?.onCancel(task)
        }
        cleanup(taskId)
    }

    /**
     * 获取任务状态。
     *
     * @param taskId 任务ID
     * @return 传输任务，如果不存在返回null
     */
    fun getTask(taskId: String): TransferTask? = tasks[taskId]

    /**
     * 获取所有任务。
     *
     * @return 任务Map
     */
    fun getAllTasks(): Map<String, TransferTask> = HashMap(tasks)

    /**
     * 取消所有上传。
     */
    fun cancelAll() {
        calls.keys.forEach { cancelUpload(it) }
    }

    private fun cleanup(taskId: String) {
        calls.remove(taskId)
        listeners.remove(taskId)
    }
}
