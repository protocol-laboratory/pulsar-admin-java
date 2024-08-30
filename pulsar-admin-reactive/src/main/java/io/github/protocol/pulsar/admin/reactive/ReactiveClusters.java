package io.github.protocol.pulsar.admin.reactive;

public class ReactiveClusters {
    private final InnerReactiveClient innerClient;

    public ReactiveClusters(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
