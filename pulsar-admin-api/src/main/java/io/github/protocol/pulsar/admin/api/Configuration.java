package io.github.protocol.pulsar.admin.api;

import io.github.openfacade.http.HttpClientEngine;

public class Configuration {
    public String host = "localhost";

    public int port;

    public boolean tlsEnabled;

    public TlsConfig tlsConfig;

    public HttpClientEngine engine;

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

    public Configuration tlsEnabled(boolean tlsEnabled) {
        this.tlsEnabled = tlsEnabled;
        return this;
    }

    public Configuration tlsConfig(TlsConfig tlsConfig) {
        this.tlsConfig = tlsConfig;
        return this;
    }

    public Configuration engine(HttpClientEngine engine) {
        this.engine = engine;
        return this;
    }
}
