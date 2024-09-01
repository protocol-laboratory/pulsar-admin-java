package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarConfig;
import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import io.github.protocol.pulsar.admin.api.TlsConfig;
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

    private PulsarAdmin createPulsarAdmin(TlsConfig tlsConfig) throws PulsarAdminException {
        return PulsarAdmin.builder()
                .port(server.getWebPort())
                .tlsEnabled(true)
                .tlsConfig(tlsConfig)
                .build();
    }

    @Test
    public void testTlsNoVerify() throws PulsarAdminException {
        TlsConfig tlsConfig = new TlsConfig(
                new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                false,
                true,
                null,
                null
        );
        PulsarAdmin pulsarAdmin = createPulsarAdmin(tlsConfig);
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsCustomProtocol() throws PulsarAdminException {
        TlsConfig tlsConfig = new TlsConfig(
                new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                false,
                true,
                new String[]{"TLSv1.2"},
                null
        );
        PulsarAdmin pulsarAdmin = createPulsarAdmin(tlsConfig);
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsCustomCiphers() throws PulsarAdminException {
        TlsConfig tlsConfig = new TlsConfig(
                new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                false,
                true,
                null,
                new String[]{"ECDHE-ECDSA-AES128-GCM-SHA256", "ECDHE-RSA-AES128-GCM-SHA256",
                        "ECDHE-ECDSA-AES256-GCM-SHA384", "ECDHE-RSA-AES256-GCM-SHA384", "ECDHE-ECDSA-CHACHA20-POLY1305",
                        "ECDHE-RSA-CHACHA20-POLY1305", "DHE-RSA-AES128-GCM-SHA256", "DHE-RSA-AES256-GCM-SHA384"}
        );
        PulsarAdmin pulsarAdmin = createPulsarAdmin(tlsConfig);
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsByVerifyAndNoHostnameVerification() throws PulsarAdminException {
        TlsConfig tlsConfig = new TlsConfig(
                new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                true,
                false,
                null,
                null
        );
        PulsarAdmin pulsarAdmin = createPulsarAdmin(tlsConfig);
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsByVerifyAndHostnameVerification() throws PulsarAdminException {
        TlsConfig tlsConfig = new TlsConfig(
                new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
                CLIENT_CERT_PASSWORD.toCharArray(),
                true,
                true,
                null,
                null
        );
        PulsarAdmin pulsarAdmin = createPulsarAdmin(tlsConfig);
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @AfterAll
    public static void teardown() throws Exception {
        server.close();
    }
}
