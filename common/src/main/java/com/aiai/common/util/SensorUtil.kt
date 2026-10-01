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

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

/**
 * 传感器工具类。
 *
 * 提供加速度计、陀螺仪、光线传感器、距离传感器等功能。
 */
object SensorUtil {

    private const val TAG = "SensorUtil"

    /**
     * 传感器数据回调。
     */
    interface SensorCallback {
        /** 传感器数据变化。 */
        fun onSensorChanged(values: FloatArray)

        /** 精度变化。 */
        fun onAccuracyChanged(accuracy: Int) {}
    }

    /**
     * 获取传感器管理器。
     */
    private fun getSensorManager(context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    /**
     * 检查传感器是否存在。
     *
     * @param context 上下文
     * @param sensorType 传感器类型
     * @return true 表示存在
     */
    fun hasSensor(context: Context, sensorType: Int): Boolean {
        val sensorManager = getSensorManager(context)
        return sensorManager.getDefaultSensor(sensorType) != null
    }

    /**
     * 获取所有传感器列表。
     *
     * @param context 上下文
     * @return 传感器列表
     */
    fun getAllSensors(context: Context): List<Sensor> {
        val sensorManager = getSensorManager(context)
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }

    /**
     * 注册加速度计监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期（微秒）
     * @return SensorEventListener
     */
    fun registerAccelerometer(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: run {
            Log.w(TAG, "Accelerometer not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注册陀螺仪监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期
     * @return SensorEventListener
     */
    fun registerGyroscope(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) ?: run {
            Log.w(TAG, "Gyroscope not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注册光线传感器监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期
     * @return SensorEventListener
     */
    fun registerLightSensor(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) ?: run {
            Log.w(TAG, "Light sensor not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注册距离传感器监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期
     * @return SensorEventListener
     */
    fun registerProximitySensor(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) ?: run {
            Log.w(TAG, "Proximity sensor not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注册磁力计监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期
     * @return SensorEventListener
     */
    fun registerMagnetometer(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) ?: run {
            Log.w(TAG, "Magnetometer not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注册压力传感器监听。
     *
     * @param context 上下文
     * @param callback 数据回调
     * @param samplingPeriodUs 采样周期
     * @return SensorEventListener
     */
    fun registerPressureSensor(
        context: Context,
        callback: SensorCallback,
        samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_NORMAL
    ): SensorEventListener? {
        val sensorManager = getSensorManager(context)
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: run {
            Log.w(TAG, "Pressure sensor not available")
            return null
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                callback.onSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                callback.onAccuracyChanged(accuracy)
            }
        }

        sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        return listener
    }

    /**
     * 注销传感器监听。
     *
     * @param context 上下文
     * @param listener 要注销的监听
     */
    fun unregisterListener(context: Context, listener: SensorEventListener?) {
        if (listener == null) return
        val sensorManager = getSensorManager(context)
        sensorManager.unregisterListener(listener)
    }

    /**
     * 获取传感器名称。
     *
     * @param sensorType 传感器类型
     * @return 传感器名称
     */
    fun getSensorName(sensorType: Int): String {
        return when (sensorType) {
            Sensor.TYPE_ACCELEROMETER -> "加速度计"
            Sensor.TYPE_GYROSCOPE -> "陀螺仪"
            Sensor.TYPE_LIGHT -> "光线传感器"
            Sensor.TYPE_PROXIMITY -> "距离传感器"
            Sensor.TYPE_MAGNETIC_FIELD -> "磁力计"
            Sensor.TYPE_PRESSURE -> "压力传感器"
            Sensor.TYPE_TEMPERATURE -> "温度传感器"
            Sensor.TYPE_RELATIVE_HUMIDITY -> "湿度传感器"
            Sensor.TYPE_AMBIENT_TEMPERATURE -> "环境温度"
            Sensor.TYPE_STEP_COUNTER -> "计步器"
            Sensor.TYPE_STEP_DETECTOR -> "步检测器"
            Sensor.TYPE_HEART_RATE -> "心率传感器"
            else -> "未知传感器($sensorType)"
        }
    }
}
