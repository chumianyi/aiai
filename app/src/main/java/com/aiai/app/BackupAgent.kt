/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.app.backup.BackupAgentHelper
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.app.backup.SharedPreferencesBackupHelper
import android.os.ParcelFileDescriptor
import com.aiai.core.util.LogUtil

/**
 * 自动备份代理
 *
 * 使用Android Backup Agent备份SharedPreferences。
 */
class BackupAgent : BackupAgentHelper() {

    companion object {
        private const val TAG = "BackupAgent"
        private const val PREFS_BACKUP_KEY = "aiai_prefs_backup"
    }

    override fun onCreate() {
        val prefsHelper = SharedPreferencesBackupHelper(this, AppConstants.PREF_NAME)
        addHelper(PREFS_BACKUP_KEY, prefsHelper)
        LogUtil.d(TAG, "BackupAgent created")
    }

    override fun onBackup(
        oldState: ParcelFileDescriptor?,
        data: BackupDataOutput?,
        newState: ParcelFileDescriptor?
    ) {
        super.onBackup(oldState, data, newState)
        LogUtil.d(TAG, "onBackup completed")
    }

    override fun onRestore(data: BackupDataInput?, appVersionCode: Int, newState: ParcelFileDescriptor?) {
        super.onRestore(data, appVersionCode, newState)
        LogUtil.d(TAG, "onRestore completed from version: $appVersionCode")
    }
}
