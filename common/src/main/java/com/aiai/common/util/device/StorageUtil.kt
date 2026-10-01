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
package com.aiai.common.util.device

import android.os.Environment
import android.os.StatFs
import java.io.File

/**
 * 存储状态工具类。
 */
object StorageUtil {

    /** 获取内部存储总空间（字节）。 */
    fun getInternalTotal(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.blockSizeLong * stat.blockCountLong
    }

    /** 获取内部存储可用空间（字节）。 */
    fun getInternalAvailable(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.blockSizeLong * stat.availableBlocksLong
    }

    /** 判断内部存储是否充足（剩余 > threshold）。 */
    fun hasEnoughSpace(threshold: Long = 100 * 1024 * 1024L): Boolean {
        return getInternalAvailable() > threshold
    }

    /** 判断外部存储是否可写。 */
    fun isExternalWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}
