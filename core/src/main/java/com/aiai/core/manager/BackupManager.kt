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
package com.aiai.core.manager

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 备份管理器。
 *
 * 提供数据备份、恢复、导出等功能。
 */
object BackupManager {

    private const val TAG = "BackupManager"
    private const val BACKUP_DIR = "backups"
    private const val MAX_BACKUP_COUNT = 5

    /**
     * 备份状态。
     */
    sealed class BackupState {
        /** 空闲。 */
        object Idle : BackupState()

        /** 正在备份。 */
        data class Backuping(val progress: Int, val message: String) : BackupState()

        /** 备份成功。 */
        data class Success(val backupFile: File) : BackupState()

        /** 备份失败。 */
        data class Error(val message: String) : BackupState()
    }

    private var context: Context? = null

    /**
     * 初始化。
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        this.context = context.applicationContext
        Log.d(TAG, "BackupManager initialized")
    }

    /**
     * 创建备份。
     *
     * @return 备份状态流
     */
    fun createBackup(): Flow<BackupState> = flow {
        emit(BackupState.Backuping(0, "准备备份..."))

        try {
            val ctx = context ?: throw Exception("Context not initialized")

            val backupDir = File(ctx.filesDir, BACKUP_DIR)
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val fileName = "backup_${dateFormat.format(Date())}.zip"
            val backupFile = File(backupDir, fileName)

            emit(BackupState.Backuping(20, "收集数据..."))

            // 模拟备份过程
            val dataDir = ctx.filesDir
            ZipOutputStream(FileOutputStream(backupFile).buffered()).use { zos ->
                dataDir.walkTopDown().forEach { file ->
                    if (file.isFile && !file.absolutePath.contains(BACKUP_DIR)) {
                        val entryName = dataDir.toURI().relativize(file.toURI()).path
                        zos.putNextEntry(ZipEntry(entryName))
                        FileInputStream(file).use { it.copyTo(zos) }
                        zos.closeEntry()
                    }
                }
            }

            emit(BackupState.Backuping(80, "清理旧备份..."))

            // 清理旧备份
            cleanOldBackups(backupDir)

            emit(BackupState.Backuping(100, "完成"))
            Log.d(TAG, "Backup created: ${backupFile.absolutePath}")
            emit(BackupState.Success(backupFile))
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed", e)
            emit(BackupState.Error(e.message ?: "备份失败"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 清理旧备份。
     */
    private fun cleanOldBackups(backupDir: File) {
        val backups = backupDir.listFiles { file -> file.name.startsWith("backup_") }
            ?.sortedByDescending { it.lastModified() } ?: return

        if (backups.size > MAX_BACKUP_COUNT) {
            backups.drop(MAX_BACKUP_COUNT).forEach { it.delete() }
        }
    }

    /**
     * 获取备份列表。
     *
     * @return 备份文件列表
     */
    fun getBackupList(): List<File> {
        val ctx = context ?: return emptyList()
        val backupDir = File(ctx.filesDir, BACKUP_DIR)
        if (!backupDir.exists()) return emptyList()
        return backupDir.listFiles { file -> file.name.startsWith("backup_") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    /**
     * 删除备份。
     *
     * @param backupFile 备份文件
     * @return 是否成功
     */
    fun deleteBackup(backupFile: File): Boolean {
        return try {
            val deleted = backupFile.delete()
            Log.d(TAG, "Backup deleted: ${backupFile.name}, success=$deleted")
            deleted
        } catch (e: Exception) {
            Log.e(TAG, "Delete backup failed", e)
            false
        }
    }

    /**
     * 清除所有备份。
     */
    fun clearAllBackups() {
        getBackupList().forEach { it.delete() }
        Log.d(TAG, "All backups cleared")
    }

    /**
     * 获取备份总大小。
     *
     * @return 总大小（字节）
     */
    fun getTotalBackupSize(): Long {
        return getBackupList().sumOf { it.length() }
    }

    /**
     * 格式化大小。
     */
    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "${bytes}B"
            bytes < 1024 * 1024 -> String.format("%.2fKB", bytes / 1024.0)
            bytes < 1024 * 1024 * 1024 -> String.format("%.2fMB", bytes / (1024.0 * 1024.0))
            else -> String.format("%.2fGB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }
}
