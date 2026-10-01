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
package com.aiai.data.manager

import android.util.Log

/**
 * 数据库备份管理器。
 *
 * 提供数据库备份和恢复功能。
 */
class DatabaseBackupManager {

    companion object {
        private const val TAG = "DatabaseBackupManager"
    }

    /**
     * 备份数据库。
     *
     * @param sourcePath 源数据库路径
     * @param backupPath 备份路径
     * @return 是否成功
     */
    fun backup(sourcePath: String, backupPath: String): Boolean {
        return try {
            val sourceFile = java.io.File(sourcePath)
            val backupFile = java.io.File(backupPath)
            if (!sourceFile.exists()) {
                Log.e(TAG, "Source database not found: $sourcePath")
                return false
            }
            backupFile.parentFile?.mkdirs()
            sourceFile.inputStream().use { input ->
                backupFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Log.d(TAG, "Database backed up to: $backupPath")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed: ${e.message}")
            false
        }
    }

    /**
     * 恢复数据库。
     *
     * @param backupPath 备份路径
     * @param targetPath 目标数据库路径
     * @return 是否成功
     */
    fun restore(backupPath: String, targetPath: String): Boolean {
        return try {
            val backupFile = java.io.File(backupPath)
            val targetFile = java.io.File(targetPath)
            if (!backupFile.exists()) {
                Log.e(TAG, "Backup file not found: $backupPath")
                return false
            }
            backupFile.inputStream().use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            Log.d(TAG, "Database restored from: $backupPath")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Restore failed: ${e.message}")
            false
        }
    }

    /**
     * 获取备份文件列表。
     *
     * @param backupDir 备份目录
     * @return 备份文件列表
     */
    fun listBackups(backupDir: String): List<BackupInfo> {
        val dir = java.io.File(backupDir)
        if (!dir.exists()) return emptyList()
        return dir.listFiles { f -> f.name.endsWith(".db") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { BackupInfo(
                fileName = it.name,
                filePath = it.absolutePath,
                sizeBytes = it.length(),
                createdAt = it.lastModified(),
            ) }
            ?: emptyList()
    }

    /**
     * 删除备份。
     */
    fun deleteBackup(filePath: String): Boolean {
        return try {
            java.io.File(filePath).delete()
        } catch (e: Exception) {
            Log.e(TAG, "Delete backup failed: ${e.message}")
            false
        }
    }

    /**
     * 备份信息数据类。
     */
    data class BackupInfo(
        val fileName: String,
        val filePath: String,
        val sizeBytes: Long,
        val createdAt: Long,
    )
}
