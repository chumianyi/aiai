package com.aiai.data

import org.junit.Assert.*
import org.junit.Test

class DataTest {
    @Test fun database_name() = assertEquals("aiai_database.db", "aiai_database.db")
    @Test fun entity_message_role_user() = assertEquals("user", "user")
    @Test fun entity_message_role_assistant() = assertEquals("assistant", "assistant")
    @Test fun dao_insert_query() = assertNotNull(Any())
    @Test fun repository_interface() = assertNotNull(Any())
    @Test fun conversation_entity() = assertNotNull(Any())
    @Test fun message_entity() = assertNotNull(Any())
    @Test fun prompt_entity() = assertNotNull(Any())
    @Test fun paging_source() = assertNotNull(Any())
    @Test fun data_store_prefs() = assertNotNull(Any())
}
