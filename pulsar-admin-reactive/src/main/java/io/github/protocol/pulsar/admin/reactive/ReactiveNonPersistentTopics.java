package io.github.protocol.pulsar.admin.reactive;

public class ReactiveNonPersistentTopics {
    private final InnerReactiveClient innerClient;

    public ReactiveNonPersistentTopics(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
