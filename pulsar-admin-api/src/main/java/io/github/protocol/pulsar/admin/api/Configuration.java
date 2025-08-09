package io.github.protocol.pulsar.admin.api;

import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.ReactorHttpClientConfig;

public class Configuration {
    public String host = "localhost";

    public int port;

    public HttpClientConfig httpClientConfig;

    public ReactorHttpClientConfig reactorHttpClientConfig;

    public Configuration() {
    }

    public Configuration host(String host) {
        this.host = host;
        return this;
    }

    public Configuration port(int port) {
        this.port = port;
        return this;
    }

    public Configuration httpClientConfig(HttpClientConfig httpClientConfig) {
        this.httpClientConfig = httpClientConfig;
        return this;
    }

    public Configuration reactorHttpClientConfig(ReactorHttpClientConfig reactorHttpClientConfig) {
        this.reactorHttpClientConfig = reactorHttpClientConfig;
        return this;
    }
}
