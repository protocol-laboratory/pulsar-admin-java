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

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClustersTest {

    private static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void getClustersTest() throws PulsarAdminException {
        Assertions.assertEquals(List.of("standalone"),
                PulsarAdmin.builder().port(SERVER.getWebPort()).build().clusters().getClusters());
    }

}
