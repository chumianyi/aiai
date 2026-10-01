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
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

/**
 * 联系人工具类。
 *
 * 提供读取、添加、删除、查询、分组等功能。
 */
object ContactUtil {

    private const val TAG = "ContactUtil"

    /**
     * 联系人数据模型。
     */
    data class Contact(
        val id: String,
        val displayName: String,
        val phoneNumber: String,
        val email: String,
        val photoUri: Uri?
    )

    /**
     * 获取所有联系人。
     *
     * @param context 上下文
     * @return 联系人列表
     */
    fun getAllContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            ),
            null, null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIndex) ?: ""
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                contacts.add(Contact(id, name, number, "", null))
            }
        }

        return contacts
    }

    /**
     * 搜索联系人。
     *
     * @param context 上下文
     * @param query 搜索关键词
     * @return 匹配的联系人列表
     */
    fun searchContacts(context: Context, query: String): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
            arrayOf("%$query%"),
            null
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                contacts.add(
                    Contact(
                        it.getString(idIndex) ?: "",
                        it.getString(nameIndex) ?: "",
                        it.getString(numberIndex) ?: "",
                        "", null
                    )
                )
            }
        }

        return contacts
    }

    /**
     * 获取联系人数量。
     *
     * @param context 上下文
     * @return 联系人数量
     */
    fun getContactCount(context: Context): Int {
        var count = 0
        val resolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(ContactsContract.Contacts._ID),
            null, null, null
        )
        cursor?.use { count = it.count }
        return count
    }

    /**
     * 添加联系人（需启动系统界面）。
     *
     * @param context 上下文
     * @param name 姓名
     * @param phoneNumber 电话号码
     * @param email 邮箱
     * @param company 公司
     * @param title 职位
     */
    fun addContact(
        context: Context,
        name: String,
        phoneNumber: String,
        email: String = "",
        company: String = "",
        title: String = ""
    ) {
        val intent = android.content.Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, name)
            putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
            putExtra(ContactsContract.Intents.Insert.EMAIL, email)
            putExtra(ContactsContract.Intents.Insert.COMPANY, company)
            putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title)
        }
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 读取联系人头像。
     *
     * @param context 上下文
     * @param contactId 联系人ID
     * @return 头像 Uri
     */
    fun getContactPhoto(context: Context, contactId: String): Uri? {
        return try {
            val resolver = context.contentResolver
            val cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.PHOTO_URI),
                "${ContactsContract.Contacts._ID} = ?",
                arrayOf(contactId),
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                    val photoUri = it.getString(photoUriIndex)
                    if (!photoUri.isNullOrEmpty()) Uri.parse(photoUri) else null
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get contact photo failed", e)
            null
        }
    }

    /**
     * 检查联系人权限。
     *
     * @param context 上下文
     * @return true 表示有权限
     */
    fun hasContactPermission(context: Context): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}
