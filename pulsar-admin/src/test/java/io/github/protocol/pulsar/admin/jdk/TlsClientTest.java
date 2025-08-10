package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarConfig;
import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.TlsConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TlsClientTest extends BaseTest {
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
    public void setup() throws Exception {
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
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder();
        builder.tlsConfig(tlsConfig);
        return PulsarAdmin.builder()
                          .port(getPort())
                          .httpClientConfig(builder.build())
                          .build();
    }

    @Test
    public void testTlsNoVerify() throws PulsarAdminException {
        TlsConfig.Builder builder = new TlsConfig.Builder();
        builder.verifyDisabled(false);
        builder.hostnameVerifyDisabled(true);
        builder.versions(null);
        builder.cipherSuites(null);
        builder.keyStore(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        builder.trustStore(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.tlsConfig(builder.build());
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(getPort())
            .httpClientConfig(clientConfigBuilder.build())
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsCustomProtocol() throws PulsarAdminException {
        TlsConfig.Builder builder = new TlsConfig.Builder();
        builder.verifyDisabled(false);
        builder.hostnameVerifyDisabled(true);
        builder.versions(new String[]{"TLSv1.2"});
        builder.cipherSuites(null);
        builder.keyStore(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        builder.trustStore(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.tlsConfig(builder.build());
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(getPort())
                .httpClientConfig(clientConfigBuilder.build())
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsCustomCiphers() throws PulsarAdminException {
        TlsConfig.Builder builder = new TlsConfig.Builder();
        builder.verifyDisabled(false);
        builder.hostnameVerifyDisabled(true);
        builder.versions(null);
        builder.cipherSuites(new String[]{"ECDHE-ECDSA-AES128-GCM-SHA256", "ECDHE-RSA-AES128-GCM-SHA256",
                        "ECDHE-ECDSA-AES256-GCM-SHA384", "ECDHE-RSA-AES256-GCM-SHA384", "ECDHE-ECDSA-CHACHA20-POLY1305",
                        "ECDHE-RSA-CHACHA20-POLY1305", "DHE-RSA-AES128-GCM-SHA256", "DHE-RSA-AES256-GCM-SHA384"});
        builder.keyStore(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        builder.trustStore(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.tlsConfig(builder.build());
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(getPort())
                .httpClientConfig(clientConfigBuilder.build())
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsByVerifyAndNoHostnameVerification() throws PulsarAdminException {
        TlsConfig.Builder builder = new TlsConfig.Builder();
        builder.verifyDisabled(true);
        builder.hostnameVerifyDisabled(false);
        builder.versions(null);
        builder.cipherSuites(null);
        builder.keyStore(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        builder.trustStore(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.tlsConfig(builder.build());
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(getPort())
                .httpClientConfig(clientConfigBuilder.build())
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testTlsByVerifyAndHostnameVerification() throws PulsarAdminException {
        TlsConfig.Builder builder = new TlsConfig.Builder();
        builder.verifyDisabled(true);
        builder.hostnameVerifyDisabled(true);
        builder.versions(null);
        builder.cipherSuites(null);
        builder.keyStore(new File(CLIENT_KEYSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        builder.trustStore(new File(CLIENT_TRUSTSTORE_FILE).getAbsolutePath(),
            CLIENT_CERT_PASSWORD.toCharArray());
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.tlsConfig(builder.build());
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder()
                .port(getPort())
                .httpClientConfig(clientConfigBuilder.build())
                .build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @AfterAll
    public void teardown() throws Exception {
        server.close();
    }
}
