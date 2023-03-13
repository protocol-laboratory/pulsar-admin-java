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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BrokersTest {
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
    public void testHealthCheckV1() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testHealthCheckV2() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V2);
    }
}
