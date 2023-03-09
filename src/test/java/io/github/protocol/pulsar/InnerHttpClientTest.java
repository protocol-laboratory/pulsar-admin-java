/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.protocol.pulsar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InnerHttpClientTest {

    @Test
    public void mapToParamsCaseEmptyMap(){
        Assertions.assertEquals("", InnerHttpClient.mapToParams());
    }

    @Test
    public void mapToParamsCaseSpecialChars(){
        Assertions.assertEquals("?a=b&a%2F=b%5C&%25=%26&%3D=%3F", InnerHttpClient.mapToParams(
                        "a", "b",
                        "a/", "b\\",
                        "%", "&",
                        "=", "?"));
    }

}
