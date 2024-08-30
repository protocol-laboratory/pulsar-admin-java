package io.github.protocol.pulsar.admin.reactive;

public class ReactiveNamespaces {
    private final InnerReactiveClient innerClient;

    public ReactiveNamespaces(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
