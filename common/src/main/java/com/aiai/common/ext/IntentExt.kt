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
package com.aiai.common.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.Browser
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.Settings

/**
 * Intent 相关扩展函数集合。
 *
 * 提供常用 Intent 构建：分享、拨号、短信、邮件、设置、市场等。
 */

// region 常用系统 Intent

/** 构建分享文本 Intent。 */
fun shareTextIntent(text: String, subject: String = ""): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
}

/** 构建拨号 Intent（需 CALL_PHONE 权限才能直接拨打）。 */
fun dialIntent(phone: String): Intent {
    return Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
}

/** 构建发送短信 Intent。 */
fun smsIntent(phone: String, body: String = ""): Intent {
    return Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")).apply {
        putExtra("sms_body", body)
    }
}

/** 构建发送邮件 Intent。 */
fun emailIntent(to: String, subject: String = "", body: String = ""): Intent {
    return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$to")).apply {
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
}

/** 构建打开浏览器 Intent。 */
fun browserIntent(url: String): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse(url))
}

/** 构建应用市场详情页 Intent。 */
fun marketIntent(packageName: String): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
}

/** 构建应用详情设置页 Intent。 */
fun appSettingsIntent(packageName: String): Intent {
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
}

/** 构建 WLAN 设置页 Intent。 */
fun wifiSettingsIntent(): Intent = Intent(Settings.ACTION_WIFI_SETTINGS)

/** 构建位置设置页 Intent。 */
fun locationSettingsIntent(): Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

/** 构建蓝牙设置页 Intent。 */
fun bluetoothSettingsIntent(): Intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)

/** 构建闹钟 Intent。 */
fun alarmIntent(message: String, hour: Int, minutes: Int): Intent {
    return Intent(AlarmClock.ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minutes)
    }
}

/** 构建添加日历事件 Intent。 */
fun calendarEventIntent(title: String, beginTime: Long, endTime: Long): Intent {
    return Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        putExtra(CalendarContract.Events.TITLE, title)
    }
}

/** 构建选择联系人 Intent。 */
fun pickContactIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }
}

/** 构建拍照 Intent。 */
fun takePictureIntent(outputUri: Uri): Intent {
    return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
    }
}

/** 构建选择图片 Intent。 */
fun pickImageIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
    }
}

// endregion

// region 安全启动

/** 安全启动 Intent，捕获 ActivityNotFoundException。 */
fun Context.startActivitySafe(intent: Intent): Boolean {
    return try {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

// endregion
