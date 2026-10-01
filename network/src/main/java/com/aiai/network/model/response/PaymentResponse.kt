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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 支付响应模型集合。
 *
 * 包含订单、退款、账单等支付相关响应数据结构。
 */
sealed class PaymentResponse {

    /**
     * 基础响应。
     */
    open class BaseResponse(
        @SerializedName("code")
        open val code: Int = 0,
        @SerializedName("message")
        open val message: String = ""
    )

    /**
     * 订单响应。
     *
     * @property orderId 订单ID
     * @property orderNo 订单编号
     * @property amount 金额（分）
     * @property currency 货币类型
     * @property status 订单状态
     * @property paymentMethod 支付方式
     * @property createdAt 创建时间
     * @property paidAt 支付时间
     */
    data class OrderResponse(
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("order_no")
        val orderNo: String = "",
        @SerializedName("amount")
        val amount: Long = 0,
        @SerializedName("currency")
        val currency: String = "CNY",
        @SerializedName("status")
        val status: String = "pending",
        @SerializedName("payment_method")
        val paymentMethod: String = "",
        @SerializedName("created_at")
        val createdAt: Long = 0,
        @SerializedName("paid_at")
        val paidAt: Long? = null
    ) : BaseResponse()

    /**
     * 订单列表响应。
     *
     * @property list 订单列表
     * @property total 总数
     * @property page 当前页
     * @property pageSize 每页数量
     */
    data class OrderListResponse(
        @SerializedName("list")
        val list: List<OrderResponse> = emptyList(),
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("page_size")
        val pageSize: Int = 20
    ) : BaseResponse()

    /**
     * 退款响应。
     *
     * @property refundId 退款ID
     * @property orderId 订单ID
     * @property amount 退款金额
     * @property reason 退款原因
     * @property status 退款状态
     * @property createdAt 创建时间
     */
    data class RefundResponse(
        @SerializedName("refund_id")
        val refundId: String = "",
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("amount")
        val amount: Long = 0,
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("status")
        val status: String = "pending",
        @SerializedName("created_at")
        val createdAt: Long = 0
    ) : BaseResponse()

    /**
     * 账单数据。
     *
     * @property billId 账单ID
     * @property type 类型（income/expense）
     * @property amount 金额
     * @property description 描述
     * @property date 日期
     */
    data class Bill(
        @SerializedName("bill_id")
        val billId: String = "",
        @SerializedName("type")
        val type: String = "expense",
        @SerializedName("amount")
        val amount: Long = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("date")
        val date: Long = 0
    )

    /**
     * 账单列表响应。
     *
     * @property bills 账单列表
     * @property totalIncome 总收入
     * @property totalExpense 总支出
     */
    data class BillListResponse(
        @SerializedName("bills")
        val bills: List<Bill> = emptyList(),
        @SerializedName("total_income")
        val totalIncome: Long = 0,
        @SerializedName("total_expense")
        val totalExpense: Long = 0
    ) : BaseResponse()

    /**
     * 支付方式。
     *
     * @property method 支付方式
     * @property name 名称
     * @property icon 图标URL
     * @property enabled 是否可用
     */
    data class PaymentMethod(
        @SerializedName("method")
        val method: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("enabled")
        val enabled: Boolean = true
    )

    /**
     * 支付方式列表响应。
     */
    data class PaymentMethodListResponse(
        @SerializedName("methods")
        val methods: List<PaymentMethod> = emptyList()
    ) : BaseResponse()

    /**
     * 支付结果响应。
     *
     * @property success 是否成功
     * @property orderId 订单ID
     * @property transactionId 交易ID
     */
    data class PaymentResultResponse(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("transaction_id")
        val transactionId: String = ""
    ) : BaseResponse()

    /**
     * 支付配置响应。
     *
     * @property appId 应用ID
     * @property merchantId 商户ID
     */
    data class PaymentConfigResponse(
        @SerializedName("app_id")
        val appId: String = "",
        @SerializedName("merchant_id")
        val merchantId: String = ""
    ) : BaseResponse()
}
