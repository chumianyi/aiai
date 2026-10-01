/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.datasource

/**
 * 远程数据源基类。
 *
 * 定义远程API数据操作的通用接口。
 *
 * @param T 数据类型
 */
abstract class RemoteDataSource<T> {

    /**
     * 从远程服务器获取数据。
     */
    abstract suspend fun fetch(): Result<T>

    /**
     * 提交数据到远程服务器。
     */
    abstract suspend fun post(data: T): Result<T>

    /**
     * 更新远程数据。
     */
    abstract suspend fun update(data: T): Result<T>

    /**
     * 删除远程数据。
     */
    abstract suspend fun delete(id: String): Result<Unit>
}
