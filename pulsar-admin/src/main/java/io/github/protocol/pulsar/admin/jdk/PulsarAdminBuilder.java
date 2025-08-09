package io.github.protocol.pulsar.admin.jdk;

import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.ReactorHttpClientConfig;

public interface PulsarAdminBuilder {
    PulsarAdmin build();

    PulsarAdminBuilder host(String host);

    PulsarAdminBuilder port(int port);

    PulsarAdminBuilder httpClientConfig(HttpClientConfig httpClientConfig);

    PulsarAdminBuilder reactorHttpClientConfig(ReactorHttpClientConfig reactorHttpClientConfig);
}
