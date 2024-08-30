package io.github.protocol.pulsar.admin.reactive;

public class ReactivePersistentTopics {
    private final InnerReactiveClient innerClient;

    public ReactivePersistentTopics(InnerReactiveClient innerClient) {
        this.innerClient = innerClient;
    }
}
