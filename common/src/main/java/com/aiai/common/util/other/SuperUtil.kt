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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 超级工具类 - 集合所有常用方法。
 */
object SuperUtil {

    // ========== Toast ==========
    fun toast(ctx: Context, msg: String) = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    fun toastLong(ctx: Context, msg: String) = Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()

    // ========== 尺寸 ==========
    fun dp2px(ctx: Context, dp: Float): Float =
        android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dp, ctx.resources.displayMetrics)
    fun px2dp(ctx: Context, px: Float): Float = px / ctx.resources.displayMetrics.density
    fun sp2px(ctx: Context, sp: Float): Float =
        android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_SP, sp, ctx.resources.displayMetrics)

    // ========== 屏幕 ==========
    fun screenWidth(ctx: Context): Int = ctx.resources.displayMetrics.widthPixels
    fun screenHeight(ctx: Context): Int = ctx.resources.displayMetrics.heightPixels
    fun statusBarHeight(ctx: Context): Int {
        var h = 0
        val id = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (id > 0) h = ctx.resources.getDimensionPixelSize(id)
        return h
    }

    // ========== 网络 ==========
    fun hasNetwork(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager
        return cm?.activeNetworkInfo?.isConnected == true
    }

    // ========== App ==========
    fun versionName(ctx: Context): String = try {
        ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName ?: "?"
    } catch (e: Exception) { "?" }

    fun versionCode(ctx: Context): Long = try {
        val info = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode
        else @Suppress("DEPRECATION") info.versionCode.toLong()
    } catch (e: Exception) { 0L }

    // ========== Intent ==========
    fun openBrowser(ctx: Context, url: String) =
        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    fun dial(ctx: Context, phone: String) =
        ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    fun share(ctx: Context, text: String) =
        ctx.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text)
        }, "分享"))

    fun openAppSettings(ctx: Context) =
        ctx.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", ctx.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })

    // ========== View ==========
    fun show(v: View) { v.visibility = View.VISIBLE }
    fun hide(v: View) { v.visibility = View.GONE }
    fun invisible(v: View) { v.visibility = View.INVISIBLE }

    // ========== 时间 ==========
    fun now(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    fun formatTime(ts: Long): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(ts))
    fun formatDate(ts: Long): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(ts))

    // ========== 加密 ==========
    fun md5(text: String): String {
        val d = java.security.MessageDigest.getInstance("MD5").digest(text.toByteArray())
        return d.joinToString("") { "%02x".format(it) }
    }

    fun sha256(text: String): String {
        val d = java.security.MessageDigest.getInstance("SHA-256").digest(text.toByteArray())
        return d.joinToString("") { "%02x".format(it) }
    }

    // ========== 字符串 ==========
    fun isPhone(s: String): Boolean = s.matches(Regex("^1[3-9]\\d{9}$"))
    fun isEmail(s: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
    fun isEmpty(s: String?): Boolean = s == null || s.isEmpty()
    fun maskPhone(s: String): String = if (isPhone(s)) s.substring(0,3)+"****"+s.substring(7) else s
}
