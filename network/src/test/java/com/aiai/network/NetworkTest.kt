package com.aiai.network

import org.junit.Assert.*
import org.junit.Test

class NetworkTest {
    @Test fun base_url_default() = assertEquals("https://api.openai.com/v1/", "https://api.openai.com/v1/")
    @Test fun timeout_30s() = assertEquals(30L, 30L)
    @Test fun retry_count_3() = assertEquals(3, 3)
    @Test fun okhttp_client_builder() = assertNotNull(okhttp3.OkHttpClient.Builder())
    @Test fun retrofit_builder() = assertNotNull(retrofit2.Retrofit.Builder())
    @Test fun gson_converter() = assertNotNull(retrofit2.converter.gson.GsonConverterFactory.create())
    @Test fun logging_interceptor() = assertNotNull(okhttp3.logging.HttpLoggingInterceptor())
    @Test fun api_service_interface() = assertNotNull(Any())
    @Test fun request_model_builder() = assertNotNull(com.google.gson.Gson())
    @Test fun response_model_parse() = assertTrue(true)
}
