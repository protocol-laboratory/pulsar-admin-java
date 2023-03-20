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

import java.io.File;

public class TlsClientTest {

    private static EmbeddedPulsarServer SERVER;

    private static String clientKeystoreFile =
            TlsClientTest.class.getClassLoader().getResource("pulsar_client_key.jks").getFile();
    private static String clientTruststoreFile =
            TlsClientTest.class.getClassLoader().getResource("pulsar_client_trust.jks").getFile();

    private static String serverKeystoreFile =
            TlsClientTest.class.getClassLoader().getResource("pulsar_server_key.jks").getFile();
    private static String serverTruststoreFile =
            TlsClientTest.class.getClassLoader().getResource("pulsar_server_trust.jks").getFile();

    private static final String CLIENT_CERT_PASSWORD = "pulsar_client_pwd";

    private static final String SERVER_CERT_PASSWORD = "pulsar_server_pwd";

    @BeforeAll
    public static void setup() throws Exception {
        EmbeddedPulsarConfig config = new EmbeddedPulsarConfig();
        config.enableTls(true);
        config.serverKeyStorePath(new File(serverKeystoreFile).getAbsolutePath());
        config.serverKeyStorePassword(SERVER_CERT_PASSWORD);
        config.serverTrustStorePath(new File(serverTruststoreFile).getAbsolutePath());
        config.serverTrustStorePassword(SERVER_CERT_PASSWORD);
        config.clientKeyStorePath(new File(clientKeystoreFile).getAbsolutePath());
        config.clientKeyStorePassword(CLIENT_CERT_PASSWORD);
        config.clientTrustStorePath(new File(clientTruststoreFile).getAbsolutePath());
        config.clientTrustStorePassword(CLIENT_CERT_PASSWORD);
        SERVER = new EmbeddedPulsarServer(config);
        SERVER.start();
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }
}
