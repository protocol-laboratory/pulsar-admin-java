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

public class PulsarAdminBuilderImpl implements PulsarAdminBuilder {
    private final Configuration conf;

    public PulsarAdminBuilderImpl() {
        this.conf = new Configuration();
    }

    @Override
    public PulsarAdmin build() {
        return new PulsarAdminImpl(conf);
    }

    @Override
    public PulsarAdminBuilder host(String host) {
        this.conf.setHost(host);
        return this;
    }

    @Override
    public PulsarAdminBuilder port(int port) {
        this.conf.setPort(port);
        return this;
    }

    @Override
    public PulsarAdminBuilder useSsl(boolean useSsl) {
        this.conf.setUseSsl(useSsl);
        return this;
    }

    @Override
    public PulsarAdminBuilder keyStorePath(String keyStorePath) {
        this.conf.setKeyStorePath(keyStorePath);
        return this;
    }

    @Override
    public PulsarAdminBuilder keyStorePassword(String keyStorePassword) {
        this.conf.setKeyStorePassword(keyStorePassword);
        return this;
    }

    @Override
    public PulsarAdminBuilder trustStorePath(String trustStorePath) {
        this.conf.setTrustStorePath(trustStorePath);
        return this;
    }

    @Override
    public PulsarAdminBuilder trustStorePassword(String trustStorePassword) {
        this.conf.setTrustStorePassword(trustStorePassword);
        return this;
    }

    @Override
    public PulsarAdminBuilder disableSslVerify(boolean disableSslVerify) {
        this.conf.setDisableSslVerify(disableSslVerify);
        return this;
    }

    @Override
    public PulsarAdminBuilder disableHostnameVerification(boolean disableHostnameVerification) {
        this.conf.setDisableHostnameVerification(disableHostnameVerification);
        return this;
    }
}
