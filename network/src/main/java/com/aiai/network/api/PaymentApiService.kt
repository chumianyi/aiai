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
package com.aiai.network.api

import com.aiai.network.model.request.PaymentRequest
import com.aiai.network.model.response.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 支付 API 服务接口。
 *
 * 提供订单创建、支付查询、退款、账单等支付相关接口。
 */
interface PaymentApiService {

    /**
     * 创建订单。
     *
     * @param request 创建订单请求体
     * @return 订单响应
     */
    @POST("payment/create-order")
    suspend fun createOrder(@Body request: PaymentRequest.CreateOrderRequest): Response<PaymentResponse.OrderResponse>

    /**
     * 查询订单状态。
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    @GET("payment/order/{orderId}")
    suspend fun queryOrder(@Path("orderId") orderId: String): Response<PaymentResponse.OrderResponse>

    /**
     * 获取订单列表。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @return 订单列表响应
     */
    @GET("payment/orders")
    suspend fun getOrderList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<PaymentResponse.OrderListResponse>

    /**
     * 申请退款。
     *
     * @param request 退款请求体
     * @return 退款响应
     */
    @POST("payment/refund")
    suspend fun requestRefund(@Body request: PaymentRequest.RefundRequest): Response<PaymentResponse.RefundResponse>

    /**
     * 查询退款状态。
     *
     * @param refundId 退款ID
     * @return 退款响应
     */
    @GET("payment/refund/{refundId}")
    suspend fun queryRefund(@Path("refundId") refundId: String): Response<PaymentResponse.RefundResponse>

    /**
     * 获取账单列表。
     *
     * @param year 年份
     * @param month 月份
     * @return 账单列表响应
     */
    @GET("payment/bills")
    suspend fun getBills(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<PaymentResponse.BillListResponse>

    /**
     * 获取支付方式列表。
     *
     * @return 支付方式列表响应
     */
    @GET("payment/methods")
    suspend fun getPaymentMethods(): Response<PaymentResponse.PaymentMethodListResponse>

    /**
     * 确认支付结果。
     *
     * @param request 确认支付请求体
     * @return 支付结果响应
     */
    @POST("payment/confirm")
    suspend fun confirmPayment(
        @Body request: PaymentRequest.ConfirmPaymentRequest
    ): Response<PaymentResponse.PaymentResultResponse>

    /**
     * 获取支付配置。
     *
     * @return 支付配置响应
     */
    @GET("payment/config")
    suspend fun getPaymentConfig(): Response<PaymentResponse.PaymentConfigResponse>

    /**
     * 取消订单。
     *
     * @param orderId 订单ID
     * @return 取消响应
     */
    @POST("payment/order/{orderId}/cancel")
    suspend fun cancelOrder(@Path("orderId") orderId: String): Response<PaymentResponse.BaseResponse>
}
