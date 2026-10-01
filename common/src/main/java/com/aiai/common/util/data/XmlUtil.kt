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
package com.aiai.common.util.data

import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * XML 工具类。
 */
object XmlUtil {

    /** 解析 XML 字符串为 Document。 */
    fun parse(xml: String): Document? {
        return try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            builder.parse(InputSource(StringReader(xml)))
        } catch (e: Exception) {
            null
        }
    }

    /** Document 转 XML 字符串。 */
    fun toString(doc: Document): String {
        return try {
            val transformer = TransformerFactory.newInstance().newTransformer()
            val writer = StringWriter()
            transformer.transform(DOMSource(doc), StreamResult(writer))
            writer.toString()
        } catch (e: Exception) {
            ""
        }
    }
}
