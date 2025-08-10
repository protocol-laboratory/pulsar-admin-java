package io.github.protocol.pulsar.admin.reactive;

import io.github.openfacade.http.ReactorHttpClient;
import io.github.openfacade.http.ReactorHttpClientFactory;
import io.github.protocol.pulsar.admin.api.Configuration;

public class InnerReactiveClient {
    private final ReactorHttpClient httpClient;

    private final String httpPrefix;

    public InnerReactiveClient(Configuration conf) {
        ReactorHttpClient client = ReactorHttpClientFactory.createReactorHttpClient(conf.reactorHttpClientConfig);
        if (conf.reactorHttpClientConfig == null || conf.reactorHttpClientConfig.tlsConfig() == null) {
            this.httpPrefix = "https://" + conf.host + ":" + conf.port;
        } else {
            this.httpPrefix = "http://" + conf.host + ":" + conf.port;
        }

        this.httpClient = client;
    }
}
