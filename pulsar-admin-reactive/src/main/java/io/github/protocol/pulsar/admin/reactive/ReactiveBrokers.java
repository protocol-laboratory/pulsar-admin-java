package io.github.protocol.pulsar.admin.reactive;

public class ReactiveBrokers {
    private final InnerReactiveClient innerClient;

    public ReactiveBrokers(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
