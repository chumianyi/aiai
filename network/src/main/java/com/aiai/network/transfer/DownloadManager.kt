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

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 下载管理器，管理文件下载任务。
 *
 * 功能：
 * - 文件下载
 * - 进度回调
 * - 暂停/恢复
 * - 取消下载
 * - 任务状态管理
 */
class DownloadManager(
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build(),
) {

    companion object {
        private const val TAG = "DownloadManager"
        private const val BUFFER_SIZE = 8192
    }

    private val tasks = ConcurrentHashMap<String, TransferTask>()
    private val calls = ConcurrentHashMap<String, Call>()
    private val listeners = ConcurrentHashMap<String, TransferListener>()
    private val downloadedBytes = ConcurrentHashMap<String, Long>()

    /**
     * 下载文件。
     *
     * @param url 下载URL
     * @param savePath 保存路径
     * @param listener 传输监听器
     * @return 任务ID
     */
    fun downloadFile(
        url: String,
        savePath: String,
        listener: TransferListener? = null,
    ): String {
        val taskId = "download_${System.currentTimeMillis()}"

        val task = TransferTask(
            taskId = taskId,
            url = url,
            filePath = savePath,
            status = TransferStatus.PENDING,
            type = TransferType.DOWNLOAD,
        )
        tasks[taskId] = task
        listener?.let { listeners[taskId] = it }
        listener?.onStart(task)

        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)
        calls[taskId] = call

        tasks[taskId] = task.copy(status = TransferStatus.RUNNING)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                tasks[taskId] = task.copy(status = TransferStatus.FAILED, error = e.message)
                listeners[taskId]?.onError(task, e.message ?: "Unknown error")
                cleanup(taskId)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    tasks[taskId] = task.copy(
                        status = TransferStatus.FAILED,
                        error = "HTTP ${response.code}",
                    )
                    listeners[taskId]?.onError(task, "HTTP ${response.code}")
                    cleanup(taskId)
                    return
                }

                val body = response.body ?: run {
                    tasks[taskId] = task.copy(status = TransferStatus.FAILED, error = "Empty body")
                    listeners[taskId]?.onError(task, "Empty response body")
                    cleanup(taskId)
                    return
                }

                val totalBytes = body.contentLength()
                var downloaded = 0L
                val file = File(savePath)
                file.parentFile?.mkdirs()

                try {
                    body.byteStream().use { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(BUFFER_SIZE)
                            var read: Int
                            var lastProgressUpdate = 0L

                            while (input.read(buffer).also { read = it } != -1) {
                                output.write(buffer, 0, read)
                                downloaded += read
                                downloadedBytes[taskId] = downloaded

                                val now = System.currentTimeMillis()
                                if (now - lastProgressUpdate > 200) {
                                    lastProgressUpdate = now
                                    val progress = if (totalBytes > 0) {
                                        (downloaded * 100 / totalBytes).toInt()
                                    } else 0
                                    tasks[taskId] = task.copy(
                                        totalBytes = totalBytes,
                                        transferredBytes = downloaded,
                                    )
                                    listeners[taskId]?.onProgress(task, progress, 0)
                                }
                            }
                            output.flush()
                        }
                    }

                    tasks[taskId] = task.copy(
                        status = TransferStatus.COMPLETED,
                        totalBytes = totalBytes,
                        transferredBytes = downloaded,
                    )
                    listeners[taskId]?.onComplete(task, savePath)
                } catch (e: IOException) {
                    tasks[taskId] = task.copy(status = TransferStatus.FAILED, error = e.message)
                    listeners[taskId]?.onError(task, e.message ?: "Download failed")
                }
                cleanup(taskId)
            }
        })

        return taskId
    }

    /**
     * 暂停下载。
     *
     * @param taskId 任务ID
     */
    fun pauseDownload(taskId: String) {
        calls[taskId]?.cancel()
        tasks[taskId]?.let { task ->
            tasks[taskId] = task.copy(status = TransferStatus.PAUSED)
            listeners[taskId]?.onPause(task)
        }
    }

    /**
     * 取消下载。
     *
     * @param taskId 任务ID
     */
    fun cancelDownload(taskId: String) {
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
     * @return 传输任务
     */
    fun getTask(taskId: String): TransferTask? = tasks[taskId]

    /**
     * 获取所有任务。
     *
     * @return 任务Map
     */
    fun getAllTasks(): Map<String, TransferTask> = HashMap(tasks)

    /**
     * 取消所有下载。
     */
    fun cancelAll() {
        calls.keys.forEach { cancelDownload(it) }
    }

    private fun cleanup(taskId: String) {
        calls.remove(taskId)
        listeners.remove(taskId)
        downloadedBytes.remove(taskId)
    }
}
