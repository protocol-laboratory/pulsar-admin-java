package io.github.protocol.pulsar.admin.reactive;

import io.github.protocol.pulsar.admin.api.TlsConfig;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Arrays;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public class SslContextUtil {

    public static SslContext build(TlsConfig config) {
        try {
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

            if (config.keyStorePath != null && config.keyStorePassword != null) {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (FileInputStream keyStoreInputStream = new FileInputStream(config.keyStorePath)) {
                    keyStore.load(keyStoreInputStream, config.keyStorePassword);
                }
                String defaultKeyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyAlgorithm);
                keyManagerFactory.init(keyStore, config.keyStorePassword);
                sslContextBuilder.keyManager(keyManagerFactory);
            }

            if (config.verifyDisabled) {
                sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            } else if (config.trustStorePath != null && config.trustStorePassword != null) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (FileInputStream trustStoreInputStream = new FileInputStream(config.trustStorePath)) {
                    trustStore.load(trustStoreInputStream, config.trustStorePassword);
                }
                String defaultTrustAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultTrustAlgorithm);
                trustManagerFactory.init(trustStore);
                sslContextBuilder.trustManager(trustManagerFactory);
            }

            if (config.versions != null) {
                sslContextBuilder.protocols(config.versions);
            }

            if (config.cipherSuites != null) {
                sslContextBuilder.ciphers(Arrays.asList(config.cipherSuites));
            }

            return sslContextBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error setting up SSL configuration", e);
        }
    }
}
