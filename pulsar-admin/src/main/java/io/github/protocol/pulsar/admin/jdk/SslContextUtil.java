package io.github.protocol.pulsar.admin.jdk;

import io.github.protocol.pulsar.admin.api.TlsConfig;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SslContextUtil {
    public static SSLContext build(TlsConfig config) {
        try {
            // Load the key store
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreFile = new FileInputStream(config.keyStorePath)) {
                keyStore.load(keyStoreFile, config.keyStorePassword);
            }

            // Set up key manager factory to use our key store
            String defaultKeyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyAlgorithm);
            keyManagerFactory.init(keyStore, config.keyStorePassword);

            // Load the trust store, if specified
            TrustManagerFactory trustManagerFactory = null;
            if (config.trustStorePath != null) {
                KeyStore trustStore = KeyStore.getInstance("JKS");
                try (FileInputStream trustStoreFile = new FileInputStream(config.trustStorePath)) {
                    trustStore.load(trustStoreFile, config.trustStorePassword);
                }

                // Set up trust manager factory to use our trust store
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
            }

            // Set up SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers;
            if (config.verifyDisabled) {
                trustManagers = new TrustManager[]{new InsecureTrustManager()};
            } else if (trustManagerFactory != null) {
                trustManagers = trustManagerFactory.getTrustManagers();
            } else {
                trustManagers = null;
            }

            // Set up SSL parameters
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());

            SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
            if (config.versions != null && config.versions.length != 0) {
                sslParameters.setProtocols(config.versions);
            }
            if (config.cipherSuites != null && config.cipherSuites.length != 0) {
                sslParameters.setCipherSuites(config.cipherSuites);
            }

            return sslContext;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
