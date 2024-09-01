package io.github.protocol.pulsar.admin.jdk;

import io.github.protocol.pulsar.admin.api.Configuration;
import io.github.protocol.pulsar.admin.api.TlsConfig;

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
        this.conf.host(host);
        return this;
    }

    @Override
    public PulsarAdminBuilder port(int port) {
        this.conf.port(port);
        return this;
    }

    @Override
    public PulsarAdminBuilder tlsEnabled(boolean useSsl) {
        this.conf.tlsEnabled(useSsl);
        return this;
    }

    @Override
    public PulsarAdminBuilder tlsConfig(TlsConfig tlsConfig) {
        this.conf.tlsConfig(tlsConfig);
        return this;
    }
}
