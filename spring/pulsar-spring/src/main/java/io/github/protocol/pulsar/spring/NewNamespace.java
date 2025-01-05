package io.github.protocol.pulsar.spring;

import lombok.Getter;

@Getter
public class NewNamespace {
    String tenant;

    String namespace;

    public NewNamespace(String tenant, String namespace) {
        this.tenant = tenant;
        this.namespace = namespace;
    }
}
