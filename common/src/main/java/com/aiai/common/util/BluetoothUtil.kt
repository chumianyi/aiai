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

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * 蓝牙工具类。
 *
 * 提供开关、扫描、配对、连接、数据收发等功能。
 */
object BluetoothUtil {

    private const val TAG = "BluetoothUtil"
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    /**
     * 蓝牙状态回调。
     */
    interface BluetoothCallback {
        /** 发现新设备。 */
        fun onDeviceFound(device: BluetoothDevice) {}

        /** 扫描完成。 */
        fun onDiscoveryFinished() {}

        /** 蓝牙状态变化。 */
        fun onStateChanged(state: Int) {}
    }

    /**
     * 获取蓝牙适配器。
     */
    fun getBluetoothAdapter(context: Context): BluetoothAdapter? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter
        } else {
            @Suppress("DEPRECATION")
            BluetoothAdapter.getDefaultAdapter()
        }
    }

    /**
     * 检查蓝牙是否可用。
     *
     * @param context 上下文
     * @return true 表示可用
     */
    fun isBluetoothSupported(context: Context): Boolean {
        return getBluetoothAdapter(context) != null
    }

    /**
     * 检查蓝牙是否已开启。
     *
     * @param context 上下文
     * @return true 表示已开启
     */
    fun isBluetoothEnabled(context: Context): Boolean {
        return getBluetoothAdapter(context)?.isEnabled == true
    }

    /**
     * 开启蓝牙。
     *
     * @param context 上下文
     * @return true 表示成功
     */
    fun enableBluetooth(context: Context): Boolean {
        val adapter = getBluetoothAdapter(context) ?: return false
        return if (!adapter.isEnabled) {
            adapter.enable()
        } else {
            true
        }
    }

    /**
     * 关闭蓝牙。
     *
     * @param context 上下文
     * @return true 表示成功
     */
    fun disableBluetooth(context: Context): Boolean {
        val adapter = getBluetoothAdapter(context) ?: return false
        return if (adapter.isEnabled) {
            adapter.disable()
        } else {
            true
        }
    }

    /**
     * 获取已配对设备列表。
     *
     * @param context 上下文
     * @return 已配对设备集合
     */
    fun getPairedDevices(context: Context): Set<BluetoothDevice> {
        val adapter = getBluetoothAdapter(context) ?: return emptySet()
        return try {
            adapter.bondedDevices
        } catch (e: Exception) {
            Log.e(TAG, "Get paired devices failed", e)
            emptySet()
        }
    }

    /**
     * 开始扫描设备。
     *
     * @param context 上下文
     * @param callback 扫描回调
     * @return BroadcastReceiver
     */
    fun startDiscovery(
        context: Context,
        callback: BluetoothCallback
    ): BroadcastReceiver {
        val adapter = getBluetoothAdapter(context)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { callback.onDeviceFound(it) }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        callback.onDiscoveryFinished()
                    }
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                        callback.onStateChanged(state)
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }

        context.registerReceiver(receiver, filter)
        adapter?.startDiscovery()

        return receiver
    }

    /**
     * 停止扫描。
     *
     * @param context 上下文
     * @param receiver 广播接收器
     */
    fun stopDiscovery(context: Context, receiver: BroadcastReceiver?) {
        val adapter = getBluetoothAdapter(context)
        adapter?.cancelDiscovery()
        receiver?.let {
            try {
                context.unregisterReceiver(it)
            } catch (e: Exception) {
                Log.e(TAG, "Unregister receiver failed", e)
            }
        }
    }

    /**
     * 蓝牙连接客户端。
     */
    class BluetoothConnectClient(context: Context, private val deviceAddress: String) {
        private val adapter = getBluetoothAdapter(context)
        private var bluetoothSocket: BluetoothSocket? = null
        private var inputStream: InputStream? = null
        private var outputStream: OutputStream? = null
        private var isConnected = false

        /**
         * 连接设备。
         *
         * @return true 表示连接成功
         */
        fun connect(): Boolean {
            return try {
                val device = adapter?.getRemoteDevice(deviceAddress) ?: return false
                bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID)
                bluetoothSocket?.connect()
                inputStream = bluetoothSocket?.inputStream
                outputStream = bluetoothSocket?.outputStream
                isConnected = true
                Log.d(TAG, "Connected to $deviceAddress")
                true
            } catch (e: IOException) {
                Log.e(TAG, "Connect failed", e)
                isConnected = false
                false
            }
        }

        /**
         * 发送数据。
         *
         * @param data 要发送的字节数组
         * @return true 表示发送成功
         */
        fun sendData(data: ByteArray): Boolean {
            return try {
                outputStream?.write(data)
                outputStream?.flush()
                true
            } catch (e: IOException) {
                Log.e(TAG, "Send data failed", e)
                false
            }
        }

        /**
         * 接收数据。
         *
         * @param buffer 接收缓冲区
         * @return 读取的字节数
         */
        fun receiveData(buffer: ByteArray): Int {
            return try {
                inputStream?.read(buffer) ?: -1
            } catch (e: IOException) {
                Log.e(TAG, "Receive data failed", e)
                -1
            }
        }

        /**
         * 断开连接。
         */
        fun disconnect() {
            try {
                inputStream?.close()
                outputStream?.close()
                bluetoothSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Disconnect failed", e)
            }
            isConnected = false
        }

        /**
         * 是否已连接。
         */
        fun isConnected(): Boolean = isConnected
    }
}
