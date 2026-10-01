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
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * Resources 扩展函数集合。
 *
 * 提供颜色/尺寸/drawable获取、主题属性、像素转换等常用资源操作。
 */

// region 像素转换

/**
 * dp 转 px（Int）。
 *
 * @param dp dp 值
 * @return px 值
 */
fun dp2px(dp: Int): Int {
    return dp2px(dp.toFloat()).toInt()
}

/**
 * px 转 dp。
 *
 * @param px px 值
 * @return dp 值
 */
fun px2dp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.density
}

/**
 * px 转 dp（Int）。
 *
 * @param px px 值
 * @return dp 值
 */
fun px2dp(px: Int): Int {
    return px2dp(px.toFloat()).toInt()
}

/**
 * sp 转 px。
 *
 * @param sp sp 值
 * @return px 值
 */
fun sp2px(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        Resources.getSystem().displayMetrics
    )
}

/**
 * sp 转 px（Int）。
 *
 * @param sp sp 值
 * @return px 值
 */
fun sp2px(sp: Int): Int {
    return sp2px(sp.toFloat()).toInt()
}

/**
 * px 转 sp。
 *
 * @param px px 值
 * @return sp 值
 */
fun px2sp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.scaledDensity
}

/**
 * px 转 sp（Int）。
 *
 * @param px px 值
 * @return sp 值
 */
fun px2sp(px: Int): Int {
    return px2sp(px.toFloat()).toInt()
}

// endregion

// region 颜色获取

/**
 * 获取颜色资源。
 *
 * @param context 上下文
 * @param resId 颜色资源ID
 * @return 颜色值
 */
fun Context.getColorCompat(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

/**
 * 获取颜色状态列表。
 *
 * @param resId 颜色资源ID
 * @return ColorStateList
 */
fun Context.getColorStateListCompat(@ColorRes resId: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, resId)
}

/**
 * 获取主题属性颜色。
 *
 * @param attrRes 属性资源ID
 * @param defaultColor 默认颜色
 * @return 颜色值
 */
fun Context.getThemeColor(attrRes: Int, defaultColor: Int = 0): Int {
    val typedValue = android.util.TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return if (typedValue.resourceId != 0) {
        getColorCompat(typedValue.resourceId)
    } else {
        typedValue.data.takeIf { it != 0 } ?: defaultColor
    }
}

// endregion

// region Drawable 获取

/**
 * 获取 Drawable 资源。
 *
 * @param resId Drawable 资源ID
 * @return Drawable 对象
 */
fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

/**
 * 获取 Drawable 并设置边界。
 *
 * @param resId Drawable 资源ID
 * @param width 宽度
 * @param height 高度
 * @return 设置好边界的 Drawable
 */
fun Context.getDrawableWithBounds(
    @DrawableRes resId: Int,
    width: Int,
    height: Int
): Drawable? {
    return getDrawableCompat(resId)?.apply {
        setBounds(0, 0, width, height)
    }
}

// endregion

// region 尺寸获取

/**
 * 获取尺寸资源。
 *
 * @param resId 尺寸资源ID
 * @return 尺寸像素值
 */
fun Context.getDimenCompat(@DimenRes resId: Int): Int {
    return resources.getDimensionPixelSize(resId)
}

/**
 * 获取尺寸资源（Float）。
 *
 * @param resId 尺寸资源ID
 * @return 尺寸像素值
 */
fun Context.getDimenFloat(@DimenRes resId: Int): Float {
    return resources.getDimension(resId)
}

// endregion

// region 字符串获取

/**
 * 获取字符串资源。
 *
 * @param resId 字符串资源ID
 * @param formatArgs 格式化参数
 * @return 字符串
 */
fun Context.getStringCompat(@StringRes resId: Int, vararg formatArgs: Any): String {
    return getString(resId, *formatArgs)
}

/**
 * 获取字符串数组资源。
 *
 * @param resId 数组资源ID
 * @return 字符串数组
 */
fun Context.getStringArray(@ArrayRes resId: Int): Array<String> {
    return resources.getStringArray(resId)
}

// endregion

// region 屏幕信息

/**
 * 获取屏幕宽度（像素）。
 */
fun Context.screenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高度（像素）。
 */
fun Context.screenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

/**
 * 获取屏幕密度。
 */
fun Context.screenDensity(): Float {
    return resources.displayMetrics.density
}

/**
 * 获取屏幕密度DPI。
 */
fun Context.screenDensityDpi(): Int {
    return resources.displayMetrics.densityDpi
}

/**
 * 获取屏幕宽度（dp）。
 */
fun Context.screenWidthDp(): Float {
    return screenWidth() / screenDensity()
}

/**
 * 获取屏幕高度（dp）。
 */
fun Context.screenHeightDp(): Float {
    return screenHeight() / screenDensity()
}

// endregion

// region 主题属性

/**
 * 获取主题属性资源ID。
 *
 * @param attrRes 属性资源ID
 * @return 资源ID
 */
fun Context.getThemeResourceId(attrRes: Int): Int {
    val typedValue = android.util.TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.resourceId
}

/**
 * 检查主题是否包含某属性。
 *
 * @param attrRes 属性资源ID
 * @return true 表示包含
 */
fun Context.hasThemeAttribute(attrRes: Int): Boolean {
    val typedValue = android.util.TypedValue()
    return theme.resolveAttribute(attrRes, typedValue, true)
}

/**
 * 获取主题样式属性值。
 *
 * @param attrRes 属性资源ID
 * @return TypedValue
 */
fun Context.getThemeAttribute(attrRes: Int): android.util.TypedValue? {
    val typedValue = android.util.TypedValue()
    return if (theme.resolveAttribute(attrRes, typedValue, true)) {
        typedValue
    } else {
        null
    }
}

// endregion

// region 其他

/**
 * 获取状态栏高度。
 *
 * @return 状态栏高度像素值
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 获取导航栏高度。
 *
 * @return 导航栏高度像素值
 */
fun Context.getNavigationBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 检查是否为平板设备。
 *
 * @return true 表示平板
 */
fun Context.isTablet(): Boolean {
    val screenSize = screenWidthDp()
    return screenSize >= 600
}

/**
 * 检查是否为大屏设备。
 *
 * @return true 表示大屏
 */
fun Context.isLargeScreen(): Boolean {
    val screenSize = screenWidthDp()
    return screenSize >= 840
}

/**
 * 检查是否为竖屏。
 *
 * @return true 表示竖屏
 */
fun Context.isPortrait(): Boolean {
    return resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
}

/**
 * 检查是否为横屏。
 *
 * @return true 表示横屏
 */
fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
}

/**
 * 获取当前语言。
 *
 * @return 语言代码
 */
fun Context.getCurrentLanguage(): String {
    return resources.configuration.locales[0].language
}

/**
 * 获取当前国家/地区。
 *
 * @return 国家代码
 */
fun Context.getCurrentCountry(): String {
    return resources.configuration.locales[0].country
}

/**
 * 获取字体缩放比例。
 *
 * @return 字体缩放比例
 */
fun Context.getFontScale(): Float {
    return resources.configuration.fontScale
}

/**
 * 获取夜间模式状态。
 *
 * @return true 表示夜间模式
 */
fun Context.isNightMode(): Boolean {
    val nightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
}

/**
 *  Raw 资源读取为字符串。
 *
 * @param resId Raw 资源ID
 * @return 字符串内容
 */
fun Context.readRawResource(resId: Int): String {
    return resources.openRawResource(resId).bufferedReader().use { it.readText() }
}

/**
 * Assets 文件读取为字符串。
 *
 * @param fileName 文件名
 * @return 字符串内容
 */
fun Context.readAssetFile(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
}

// endregion
