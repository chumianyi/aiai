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
 * 本地数据源基类。
 *
 * 定义本地数据操作的通用接口。
 *
 * @param T 数据类型
 * @param ID 主键类型
 */
abstract class LocalDataSource<T, ID> {

    /**
     * 根据ID查询实体。
     */
    abstract suspend fun getById(id: ID): T?

    /**
     * 插入实体。
     */
    abstract suspend fun insert(entity: T)

    /**
     * 插入多个实体。
     */
    abstract suspend fun insertAll(entities: List<T>)

    /**
     * 更新实体。
     */
    abstract suspend fun update(entity: T)

    /**
     * 删除实体。
     */
    abstract suspend fun delete(entity: T)

    /**
     * 根据ID删除。
     */
    abstract suspend fun deleteById(id: ID)

    /**
     * 获取所有实体。
     */
    abstract suspend fun getAll(): List<T>

    /**
     * 清除所有数据。
     */
    abstract suspend fun clear()
}
