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

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import java.util.Locale

/**
 * 位置工具类。
 *
 * 提供 GPS/网络定位、权限检查、距离计算、地理编码等功能。
 */
object LocationUtil {

    private const val TAG = "LocationUtil"
    private const val MIN_TIME_MS = 1000L
    private const val MIN_DISTANCE_M = 1f

    /**
     * 定位回调接口。
     */
    interface LocationCallback {
        /** 定位成功。 */
        fun onLocationChanged(location: Location)

        /** 定位失败。 */
        fun onLocationFailed(error: String)
    }

    /**
     * 检查定位权限。
     *
     * @param context 上下文
     * @return true 表示有权限
     */
    fun hasLocationPermission(context: Context): Boolean {
        val finePermission = context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermission = context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return finePermission == android.content.pm.PackageManager.PERMISSION_GRANTED ||
                coarsePermission == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查 GPS 是否开启。
     *
     * @param context 上下文
     * @return true 表示已开启
     */
    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 检查网络定位是否开启。
     *
     * @param context 上下文
     * @return true 表示已开启
     */
    fun isNetworkEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * 获取最后已知位置。
     *
     * @param context 上下文
     * @return 最后位置，未获取到返回 null
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(context: Context): Location? {
        if (!hasLocationPermission(context)) return null

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var bestLocation: Location? = null

        try {
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            bestLocation = when {
                gpsLocation != null && networkLocation != null -> {
                    if (gpsLocation.time > networkLocation.time) gpsLocation else networkLocation
                }
                gpsLocation != null -> gpsLocation
                networkLocation != null -> networkLocation
                passiveLocation != null -> passiveLocation
                else -> null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get last known location failed", e)
        }

        return bestLocation
    }

    /**
     * 请求单次定位。
     *
     * @param context 上下文
     * @param callback 定位回调
     */
    @SuppressLint("MissingPermission")
    fun requestSingleLocation(
        context: Context,
        callback: LocationCallback
    ) {
        if (!hasLocationPermission(context)) {
            callback.onLocationFailed("没有定位权限")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                callback.onLocationChanged(location)
                locationManager.removeUpdates(this)
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {
                callback.onLocationFailed("定位服务未开启")
            }
        }

        try {
            if (isGpsEnabled(context)) {
                locationManager.requestSingleUpdate(
                    LocationManager.GPS_PROVIDER,
                    listener,
                    context.mainLooper
                )
            } else if (isNetworkEnabled(context)) {
                locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    listener,
                    context.mainLooper
                )
            } else {
                callback.onLocationFailed("请开启定位服务")
            }
        } catch (e: Exception) {
            callback.onLocationFailed("定位失败: ${e.message}")
        }
    }

    /**
     * 计算两个坐标之间的距离。
     *
     * @param startLat 起点纬度
     * @param startLng 起点经度
     * @param endLat 终点纬度
     * @param endLng 终点经度
     * @return 距离（米）
     */
    fun calculateDistance(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    /**
     * 计算两个 Location 之间的距离。
     *
     * @param start 起点
     * @param end 终点
     * @return 距离（米）
     */
    fun calculateDistance(start: Location, end: Location): Float {
        return start.distanceTo(end)
    }

    /**
     * 地理编码（坐标转地址）。
     *
     * @param context 上下文
     * @param latitude 纬度
     * @param longitude 经度
     * @return 地址列表
     */
    fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double,
        maxResults: Int = 1
    ): List<android.location.Address> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latitude, longitude, maxResults) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Reverse geocode failed", e)
            emptyList()
        }
    }

    /**
     * 地址编码（地址转坐标）。
     *
     * @param context 上下文
     * @param address 地址文本
     * @return 地址列表
     */
    fun geocodeAddress(
        context: Context,
        address: String,
        maxResults: Int = 1
    ): List<android.location.Address> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            geocoder.getFromLocationName(address, maxResults) ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Geocode address failed", e)
            emptyList()
        }
    }

    /**
     * 格式化距离显示。
     *
     * @param distance 距离（米）
     * @return 格式化后的距离文本
     */
    fun formatDistance(distance: Float): String {
        return when {
            distance < 1000 -> "${distance.toInt()}m"
            distance < 10000 -> String.format("%.1fkm", distance / 1000)
            else -> "${(distance / 1000).toInt()}km"
        }
    }

    /**
     * 检查是否为模拟位置。
     *
     * @param location 位置
     * @return true 表示是模拟位置
     */
    fun isMockLocation(location: Location): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            location.isMock
        } else {
            @Suppress("DEPRECATION")
            location.isFromMockProvider
        }
    }

    /**
     * 获取位置的经纬度文本。
     *
     * @param location 位置
     * @return 格式化文本
     */
    fun formatLocation(location: Location): String {
        return "Lat: ${"%.6f".format(location.latitude)}, Lng: ${"%.6f".format(location.longitude)}"
    }
}
