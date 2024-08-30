package io.github.protocol.pulsar.admin.jdk;

import io.github.protocol.pulsar.admin.api.TlsConfig;

public interface PulsarAdminBuilder {
    PulsarAdmin build();

    PulsarAdminBuilder host(String host);

    PulsarAdminBuilder port(int port);

    PulsarAdminBuilder useSsl(boolean useSsl);

    PulsarAdminBuilder tlsConfig(TlsConfig tlsConfig);

}
