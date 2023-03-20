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

import io.github.embedded.pulsar.core.EmbeddedPulsarConfig;
import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TlsClientTest {

    private static EmbeddedPulsarServer server;

    private static final String CLIENT_KEYSTORE_FILE =
            TlsClientTest.class.getClassLoader().getResource("pulsar_client_key.jks").getFile();
    private static final String CLIENT_TRUSTSTORE_FILE =
            TlsClientTest.class.getClassLoader().getResource("pulsar_client_trust.jks").getFile();

    private static final String SERVER_KEYSTORE_FILE =
            TlsClientTest.class.getClassLoader().getResource("pulsar_server_key.jks").getFile();
    private static final String SERVER_TRUSTSTORE_FILE =
            TlsClientTest.class.getClassLoader().getResource("pulsar_server_trust.jks").getFile();

    private static final String CLIENT_CERT_PASSWORD = "pulsar_client_pwd";

    private static final String SERVER_CERT_PASSWORD = "pulsar_server_pwd";

    @BeforeAll
    public static void setup() throws Exception {
        EmbeddedPulsarConfig config = new EmbeddedPulsarConfig();
        config.enableTls(true);
        config.serverKeyStorePath(new File(SERVER_KEYSTORE_FILE).getAbsolutePath());
        config.serverKeyStorePassword(SERVER_CERT_PASSWORD);
        config.serverTrustStorePath(new File(SERVER_TRUSTSTORE_FILE).getAbsolutePath());
        config.serverTrustStorePassword(SERVER_CERT_PASSWORD);
        config.clientKeyStorePath(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath());
        config.clientKeyStorePassword(CLIENT_CERT_PASSWORD);
        config.clientTrustStorePath(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath());
        config.clientTrustStorePassword(CLIENT_CERT_PASSWORD);
        server = new EmbeddedPulsarServer(config);
        server.start();
    }

    @Test
    public void testTlsClient() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(server.getWebPort())
                .useSsl(true)
                .keyStorePath(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath())
                .keyStorePassword(CLIENT_CERT_PASSWORD)
                .disableSslVerify(true)
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @AfterAll
    public static void teardown() throws Exception {
        server.close();
    }
}
