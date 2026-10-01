/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log

/**
 * 文档工具类。
 *
 * 提供 SAF 框架、文件选择、目录访问、权限持久化等功能。
 */
object DocumentUtil {

    private const val TAG = "DocumentUtil"

    /**
     * 打开文件选择器。
     *
     * @param context 上下文
     * @param mimeTypes 允许的 MIME 类型
     * @param allowMultiple 是否允许多选
     */
    fun openFilePicker(
        context: android.app.Activity,
        mimeTypes: Array<String> = arrayOf("*/*"),
        allowMultiple: Boolean = false,
        requestCode: Int = 1001
    ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
        }
        context.startActivityForResult(intent, requestCode)
    }

    /**
     * 打开目录选择器。
     *
     * @param context Activity
     * @param requestCode 请求码
     */
    fun openDirectoryPicker(
        context: android.app.Activity,
        requestCode: Int = 1002
    ) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        context.startActivityForResult(intent, requestCode)
    }

    /**
     * 持久化目录权限。
     *
     * @param context 上下文
     * @param uri 目录 Uri
     */
    fun persistDirectoryPermission(context: Context, uri: Uri) {
        try {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flags)
            Log.d(TAG, "Directory permission persisted: $uri")
        } catch (e: Exception) {
            Log.e(TAG, "Persist permission failed", e)
        }
    }

    /**
     * 获取文档名称。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @return 文档名称
     */
    fun getDocumentName(context: Context, uri: Uri): String {
        var name = ""
        val resolver = context.contentResolver

        when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> {
                val cursor: Cursor? = resolver.query(
                    uri,
                    arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME),
                    null, null, null
                )
                cursor?.use {
                    if (it.moveToFirst()) {
                        val index = it.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                        if (index >= 0) name = it.getString(index) ?: ""
                    }
                }
            }
            ContentResolver.SCHEME_FILE -> {
                name = uri.lastPathSegment ?: ""
            }
        }

        return name
    }

    /**
     * 获取文档大小。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @return 文件大小（字节）
     */
    fun getDocumentSize(context: Context, uri: Uri): Long {
        var size = 0L
        val resolver = context.contentResolver

        val cursor: Cursor? = resolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_SIZE),
            null, null, null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE)
                if (index >= 0) size = it.getLong(index)
            }
        }

        return size
    }

    /**
     * 读取文档文本内容。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @return 文本内容
     */
    fun readDocumentText(context: Context, uri: Uri): String {
        return try {
            context.contentResolver.openInputStream(uri)?.bufferedReader().use {
                it?.readText() ?: ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Read document failed", e)
            ""
        }
    }

    /**
     * 写入文档文本内容。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @param content 要写入的内容
     * @return true 表示成功
     */
    fun writeDocumentText(context: Context, uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri, "wt")?.bufferedWriter().use {
                it?.write(content)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Write document failed", e)
            false
        }
    }

    /**
     * 创建新文档。
     *
     * @param context Activity
     * @param fileName 文件名
     * @param mimeType MIME 类型
     * @param requestCode 请求码
     */
    fun createDocument(
        context: android.app.Activity,
        fileName: String,
        mimeType: String = "text/plain",
        requestCode: Int = 1003
    ) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = mimeType
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        context.startActivityForResult(intent, requestCode)
    }

    /**
     * 删除文档。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @return true 表示成功
     */
    fun deleteDocument(context: Context, uri: Uri): Boolean {
        return try {
            DocumentsContract.deleteDocument(context.contentResolver, uri)
        } catch (e: Exception) {
            Log.e(TAG, "Delete document failed", e)
            false
        }
    }

    /**
     * 检查是否为目录。
     *
     * @param context 上下文
     * @param uri 文档 Uri
     * @return true 表示是目录
     */
    fun isDirectory(context: Context, uri: Uri): Boolean {
        val resolver = context.contentResolver
        val cursor = resolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_MIME_TYPE),
            null, null, null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE)
                if (index >= 0) {
                    return it.getString(index) == DocumentsContract.Document.MIME_TYPE_DIR
                }
            }
        }
        return false
    }

    /**
     * 列出目录下的文件。
     *
     * @param context 上下文
     * @param treeUri 目录 Uri
     * @return 文件 Uri 列表
     */
    fun listFilesInDirectory(context: Context, treeUri: Uri): List<Uri> {
        val files = mutableListOf<Uri>()
        val resolver = context.contentResolver

        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            treeUri,
            DocumentsContract.getTreeDocumentId(treeUri)
        )

        val cursor = resolver.query(
            childrenUri,
            arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),
            null, null, null
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
            while (it.moveToNext()) {
                val docId = it.getString(idIndex)
                val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, docId)
                files.add(docUri)
            }
        }

        return files
    }
}
