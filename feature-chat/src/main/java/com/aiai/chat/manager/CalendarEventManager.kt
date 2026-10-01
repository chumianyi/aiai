/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 日历事件管理器
 *
 * 管理从消息中提取的日历事件。
 */
class CalendarEventManager(private val context: Context) {

    data class CalendarEvent(
        val id: String,
        val title: String,
        val description: String = "",
        val startTime: Long,
        val endTime: Long,
        val location: String = ""
    )

    private val _events = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val events: StateFlow<List<CalendarEvent>> = _events.asStateFlow()

    fun addEvent(event: CalendarEvent) {
        _events.value = _events.value + event
    }

    fun removeEvent(eventId: String) {
        _events.value = _events.value.filterNot { it.id == eventId }
    }

    fun getEventsOnDate(date: Long): List<CalendarEvent> {
        return _events.value.filter {
            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = it.startTime
            val eventDay = cal.get(java.util.Calendar.DAY_OF_YEAR)
            val eventYear = cal.get(java.util.Calendar.YEAR)
            cal.timeInMillis = date
            val targetDay = cal.get(java.util.Calendar.DAY_OF_YEAR)
            val targetYear = cal.get(java.util.Calendar.YEAR)
            eventDay == targetDay && eventYear == targetYear
        }
    }

    fun clearAll() {
        _events.value = emptyList()
    }
}
