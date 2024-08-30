package io.github.protocol.pulsar.admin.reactive;

public class ReactiveTenants {
    private final InnerReactiveClient innerClient;

    public ReactiveTenants(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
