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
package com.aiai.common.util

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.util.Log

/**
 * 小组件工具类。
 *
 * 提供更新、配置、数据传递等功能。
 */
object AppWidgetUtil {

    private const val TAG = "AppWidgetUtil"

    /**
     * 更新所有指定类型的小组件。
     *
     * @param context 上下文
     * @param provider 小组件 Provider 类
     * @param remoteViews RemoteViews
     */
    fun updateAllWidgets(
        context: Context,
        provider: Class<out AppWidgetProvider>,
        remoteViews: RemoteViews
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, provider)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        Log.d(TAG, "Updated ${appWidgetIds.size} widgets")
    }

    /**
     * 更新指定小组件。
     *
     * @param context 上下文
     * @param appWidgetId 小组件ID
     * @param remoteViews RemoteViews
     */
    fun updateWidget(
        context: Context,
        appWidgetId: Int,
        remoteViews: RemoteViews
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    /**
     * 获取所有小组件ID。
     *
     * @param context 上下文
     * @param provider 小组件 Provider 类
     * @return 小组件ID数组
     */
    fun getWidgetIds(
        context: Context,
        provider: Class<out AppWidgetProvider>
    ): IntArray {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, provider)
        return appWidgetManager.getAppWidgetIds(componentName)
    }

    /**
     * 创建点击意图 PendingIntent。
     *
     * @param context 上下文
     * @param intent 意图
     * @param requestCode 请求码
     * @return PendingIntent
     */
    fun createPendingIntent(
        context: Context,
        intent: Intent,
        requestCode: Int = 0
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * 创建打开 Activity 的 PendingIntent。
     *
     * @param context 上下文
     * @param activityClass 目标 Activity
     * @param requestCode 请求码
     * @return PendingIntent
     */
    fun createActivityPendingIntent(
        context: Context,
        activityClass: Class<*>,
        requestCode: Int = 0
    ): PendingIntent {
        val intent = Intent(context, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * 触发小组件更新。
     *
     * @param context 上下文
     * @param provider 小组件 Provider 类
     */
    fun triggerWidgetUpdate(
        context: Context,
        provider: Class<out AppWidgetProvider>
    ) {
        val intent = Intent(context, provider).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val appWidgetIds = getWidgetIds(context, provider)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        context.sendBroadcast(intent)
    }

    /**
     * 获取小组件数量。
     *
     * @param context 上下文
     * @param provider 小组件 Provider 类
     * @return 数量
     */
    fun getWidgetCount(
        context: Context,
        provider: Class<out AppWidgetProvider>
    ): Int {
        return getWidgetIds(context, provider).size
    }

    /**
     * 检查小组件是否已添加到桌面。
     *
     * @param context 上下文
     * @param provider 小组件 Provider 类
     * @return true 表示已添加
     */
    fun isWidgetAdded(
        context: Context,
        provider: Class<out AppWidgetProvider>
    ): Boolean {
        return getWidgetCount(context, provider) > 0
    }

    /**
     * 创建 RemoteViews 服务意图。
     *
     * @param context 上下文
     * @param widgetId 小组件ID
     * @param serviceClass 服务类
     * @return Intent
     */
    fun createRemoteServiceIntent(
        context: Context,
        widgetId: Int,
        serviceClass: Class<*>
    ): Intent {
        return Intent(context, serviceClass).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
    }

    /**
     * 设置小组件 TextView 文本。
     *
     * @param remoteViews RemoteViews
     * @param viewId View ID
     * @param text 文本内容
     */
    fun setWidgetText(
        remoteViews: RemoteViews,
        viewId: Int,
        text: String
    ) {
        remoteViews.setTextViewText(viewId, text)
    }

    /**
     * 设置小组件 ImageView 图片。
     *
     * @param remoteViews RemoteViews
     * @param viewId View ID
     * @param resId 图片资源
     */
    fun setWidgetImage(
        remoteViews: RemoteViews,
        viewId: Int,
        resId: Int
    ) {
        remoteViews.setImageViewResource(viewId, resId)
    }

    /**
     * 设置小组件 View 是否可见。
     *
     * @param remoteViews RemoteViews
     * @param viewId View ID
     * @param visible 是否可见
     */
    fun setWidgetViewVisibility(
        remoteViews: RemoteViews,
        viewId: Int,
        visible: Boolean
    ) {
        remoteViews.setViewVisibility(viewId, if (visible) View.VISIBLE else View.GONE)
    }
}
