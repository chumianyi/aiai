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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 百宝箱工具类 V18。
 */
object ToolboxV18 {
    fun toast(c: Context, m: String) = Toast.makeText(c, m, Toast.LENGTH_SHORT).show()
    fun toastL(c: Context, m: String) = Toast.makeText(c, m, Toast.LENGTH_LONG).show()
    fun dp2px(c: Context, dp: Float): Float = android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dp, c.resources.displayMetrics)
    fun px2dp(c: Context, px: Float): Float = px / c.resources.displayMetrics.density
    fun sp2px(c: Context, sp: Float): Float = android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_SP, sp, c.resources.displayMetrics)
    fun screenW(c: Context): Int = c.resources.displayMetrics.widthPixels
    fun screenH(c: Context): Int = c.resources.displayMetrics.heightPixels
    fun statusBarH(c: Context): Int { var h=0; val id=c.resources.getIdentifier("status_bar_height","dimen","android"); if(id>0) h=c.resources.getDimensionPixelSize(id); return h }
    fun hasNet(c: Context): Boolean { val cm=c.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager; return cm?.activeNetworkInfo?.isConnected==true }
    fun vName(c: Context): String = try { c.packageManager.getPackageInfo(c.packageName,0).versionName?:"?" } catch(e:Exception) { "?" }
    fun vCode(c: Context): Long = try { val i=c.packageManager.getPackageInfo(c.packageName,0); if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P) i.longVersionCode else @Suppress("DEPRECATION") i.versionCode.toLong() } catch(e:Exception) { 0L }
    fun browser(c: Context, u: String) = c.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(u)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    fun dial(c: Context, p: String) = c.startActivity(Intent(Intent.ACTION_DIAL,Uri.parse("tel:$p")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    fun share(c: Context, t: String) = c.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply{type="text/plain";putExtra(Intent.EXTRA_TEXT,t)},"分享"))
    fun appSettings(c: Context) = c.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply{data=Uri.fromParts("package",c.packageName,null);addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)})
    fun show(v: View) { v.visibility=View.VISIBLE }
    fun hide(v: View) { v.visibility=View.GONE }
    fun invisible(v: View) { v.visibility=View.INVISIBLE }
    fun now(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(Date())
    fun fmt(ts: Long): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(Date(ts))
    fun fmtD(ts: Long): String = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Date(ts))
    fun rel(ts: Long): String { val d=System.currentTimeMillis()-ts; return when{d<60_000->"刚刚";d<3_600_000->"${d/60_000}分钟前";d<86_400_000->"${d/3_600_000}小时前";d<7*86_400_000->"${d/86_400_000}天前";else->fmtD(ts)} }
    fun md5(s: String): String = java.security.MessageDigest.getInstance("MD5").digest(s.toByteArray()).joinToString(""){"%02x".format(it)}
    fun sha1(s: String): String = java.security.MessageDigest.getInstance("SHA-1").digest(s.toByteArray()).joinToString(""){"%02x".format(it)}
    fun sha256(s: String): String = java.security.MessageDigest.getInstance("SHA-256").digest(s.toByteArray()).joinToString(""){"%02x".format(it)}
    fun isPhone(s: String): Boolean = s.matches(Regex("^1[3-9]\\d{9}$"))
    fun isEmail(s: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
    fun isUrl(s: String): Boolean = android.util.Patterns.WEB_URL.matcher(s).matches()
    fun isEmpty(s: String?): Boolean = s==null||s.isEmpty()
    fun isBlank(s: String?): Boolean = s==null||s.isBlank()
    fun maskPhone(s: String): String = if(isPhone(s)) s.substring(0,3)+"****"+s.substring(7) else s
    fun ellipsize(s: String, max: Int): String = if(s.length>max) s.substring(0,max)+"..." else s
    fun readFile(f: File): String? = try { FileInputStream(f).bufferedReader(Charsets.UTF_8).use{it.readText()} } catch(e:Exception){null}
    fun writeFile(f: File, c: String): Boolean = try { f.parentFile?.mkdirs(); FileOutputStream(f).bufferedWriter(Charsets.UTF_8).use{it.write(c)}; true } catch(e:Exception){false}
    fun delFile(f: File): Boolean = try { if(f.isDirectory) f.listFiles()?.forEach{delFile(it)}; f.delete() } catch(e:Exception){false}
    fun fileSize(bytes: Long): String { if(bytes<=0) return "0 B"; val u=arrayOf("B","KB","MB","GB"); var s=bytes.toDouble(); var i=0; while(s>=1024&&i<u.size-1){s/=1024;i++}; return String.format("%.1f %s",s,u[i]) }
    fun jsonObj(s: String): JSONObject? = try { JSONObject(s) } catch(e:Exception){null}
    fun jsonArr(s: String): JSONArray? = try { JSONArray(s) } catch(e:Exception){null}
    fun fmtNum(n: Long): String = java.text.DecimalFormat("#,##0").format(n)
    fun fmtPct(v: Double): String = java.text.DecimalFormat("0.0%").format(v)
    fun brand(): String = Build.BRAND
    fun model(): String = Build.MODEL
    fun sdk(): Int = Build.VERSION.SDK_INT
}
