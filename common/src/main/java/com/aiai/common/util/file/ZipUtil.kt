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
package com.aiai.common.util.file

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * ZIP 压缩/解压工具类。
 */
object ZipUtil {

    /**
     * 压缩文件或目录到 zip 文件。
     */
    fun zip(src: File, destZip: File): Boolean {
        return try {
            destZip.parentFile?.mkdirs()
            ZipOutputStream(FileOutputStream(destZip).buffered()).use { zos ->
                addToZip(src, src.name, zos)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun addToZip(file: File, path: String, zos: ZipOutputStream) {
        if (file.isDirectory) {
            val children = file.listFiles() ?: return
            if (children.isEmpty()) {
                zos.putNextEntry(ZipEntry("$path/"))
                zos.closeEntry()
            } else {
                children.forEach { addToZip(it, "$path/${it.name}", zos) }
            }
        } else {
            zos.putNextEntry(ZipEntry(path))
            FileInputStream(file).use { it.copyTo(zos) }
            zos.closeEntry()
        }
    }

    /**
     * 解压 zip 文件到目标目录。
     */
    fun unzip(zipFile: File, destDir: File): Boolean {
        return try {
            destDir.mkdirs()
            ZipInputStream(FileInputStream(zipFile).buffered()).use { zis ->
                var entry: ZipEntry? = zis.nextEntry
                while (entry != null) {
                    val outFile = File(destDir, entry.name)
                    if (entry.isDirectory) {
                        outFile.mkdirs()
                    } else {
                        outFile.parentFile?.mkdirs()
                        FileOutputStream(outFile).use { zis.copyTo(it) }
                    }
                    zis.closeEntry()
                    entry = zis.nextEntry
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
