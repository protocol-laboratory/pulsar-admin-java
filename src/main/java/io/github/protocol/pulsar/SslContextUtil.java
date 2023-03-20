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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class SslContextUtil {
    public static SSLContext buildFromJks(String keyStorePath,
                                          String keyStorePassword,
                                          String trustStorePath,
                                          String trustStorePassword,
                                          boolean disableSslVerify,
                                          String[] tlsProtocols,
                                          String[] tlsCiphers) {
        try {
            // Load the key store
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream keyStoreFile = new FileInputStream(keyStorePath)) {
                keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
            }

            // Set up key manager factory to use our key store
            String defaultKeyAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(defaultKeyAlgorithm);
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            // Load the trust store, if specified
            TrustManagerFactory trustManagerFactory = null;
            if (trustStorePath != null) {
                KeyStore trustStore = KeyStore.getInstance("JKS");
                try (FileInputStream trustStoreFile = new FileInputStream(trustStorePath)) {
                    trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
                }

                // Set up trust manager factory to use our trust store
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
            }

            // Set up SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManager[] trustManagers;
            if (disableSslVerify) {
                trustManagers = new TrustManager[] { new InsecureTrustManager() };
            } else if (trustManagerFactory != null) {
                trustManagers = trustManagerFactory.getTrustManagers();
            } else {
                trustManagers = null;
            }

            // Set up SSL parameters
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());

            SSLParameters sslParameters = sslContext.getDefaultSSLParameters();
            if (tlsProtocols != null && tlsProtocols.length != 0) {
                sslParameters.setProtocols(tlsProtocols);
            }
            if (tlsCiphers != null && tlsCiphers.length != 0) {
                sslParameters.setCipherSuites(tlsCiphers);
            }

            if (disableSslVerify) {
                sslContext.getDefaultSSLParameters().setEndpointIdentificationAlgorithm(null);
            }

            return sslContext;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
