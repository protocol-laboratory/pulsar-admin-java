package io.github.protocol.pulsar.admin.reactive;

import io.github.protocol.pulsar.admin.api.Configuration;

public class ReactivePulsarAdmin {
    private final InnerReactiveClient reactorClient;

    public ReactivePulsarAdmin(Configuration conf) {
        this.reactorClient = new InnerReactiveClient(conf);
    }

    public ReactiveClusters clusters() {
        return new ReactiveClusters(reactorClient);
    }

    public ReactiveBrokers brokers() {
        return new ReactiveBrokers(reactorClient);
    }

    public ReactiveTenants tenants() {
        return new ReactiveTenants(reactorClient);
    }

    public ReactiveNamespaces namespaces() {
        return new ReactiveNamespaces(reactorClient);
    }

    public ReactivePersistentTopics persistentTopics() {
        return new ReactivePersistentTopics(reactorClient);
    }

    public ReactiveNonPersistentTopics nonPersistentTopics() {
        return new ReactiveNonPersistentTopics(reactorClient);
    }

}
