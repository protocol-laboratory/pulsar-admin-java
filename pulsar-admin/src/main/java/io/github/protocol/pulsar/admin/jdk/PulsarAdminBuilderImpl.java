package io.github.protocol.pulsar.admin.jdk;

import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.ReactorHttpClientConfig;
import io.github.protocol.pulsar.admin.api.Configuration;

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
    public PulsarAdminBuilder httpClientConfig(HttpClientConfig httpClientConfig) {
        this.conf.httpClientConfig(httpClientConfig);
        return this;
    }

    @Override
    public PulsarAdminBuilder reactorHttpClientConfig(ReactorHttpClientConfig reactorHttpClientConfig) {
        this.conf.reactorHttpClientConfig(reactorHttpClientConfig);
        return this;
    }
}
