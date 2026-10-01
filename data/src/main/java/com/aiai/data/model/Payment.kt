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
package com.aiai.data.model

/**
 * 支付域模型。
 *
 * 表示订单、退款、账单等支付相关数据。
 */
data class Payment(
    val orderId: String,
    val orderNo: String,
    val amount: Long,
    val currency: String = "CNY",
    val status: PaymentStatus,
    val paymentMethod: String,
    val createdAt: Long,
    val paidAt: Long? = null,
    val productName: String = "",
    val productDescription: String = ""
)

/**
 * 支付状态枚举。
 */
enum class PaymentStatus {
    /** 待支付。 */
    PENDING,
    /** 已支付。 */
    PAID,
    /** 已取消。 */
    CANCELLED,
    /** 已退款。 */
    REFUNDED,
    /** 退款中。 */
    REFUNDING,
    /** 支付失败。 */
    FAILED
}

/**
 * 退款记录。
 *
 * @property refundId 退款ID
 * @property orderId 订单ID
 * @property amount 退款金额
 * @property reason 退款原因
 * @property status 退款状态
 * @property createdAt 创建时间
 */
data class Refund(
    val refundId: String,
    val orderId: String,
    val amount: Long,
    val reason: String,
    val status: RefundStatus,
    val createdAt: Long
)

/**
 * 退款状态枚举。
 */
enum class RefundStatus {
    /** 待审核。 */
    PENDING,
    /** 退款中。 */
    PROCESSING,
    /** 已退款。 */
    APPROVED,
    /** 已拒绝。 */
    REJECTED
}

/**
 * 账单条目。
 *
 * @property billId 账单ID
 * @property type 类型
 * @property amount 金额
 * @property description 描述
 * @property date 日期
 */
data class Bill(
    val billId: String,
    val type: String,
    val amount: Long,
    val description: String,
    val date: Long
)
