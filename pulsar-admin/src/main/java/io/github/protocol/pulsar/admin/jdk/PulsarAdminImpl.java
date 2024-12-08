package io.github.protocol.pulsar.admin.jdk;

import io.github.protocol.pulsar.admin.api.Configuration;

public class PulsarAdminImpl implements PulsarAdmin {

    private final Clusters clusters;

    private final Brokers brokers;

    private final Tenants tenants;

    private final Namespaces namespaces;

    private final PersistentTopics persistentTopics;

    private final NonPersistentTopics nonPersistentTopics;

    PulsarAdminImpl(Configuration conf) {
        InnerHttpClient innerHttpClient = new InnerHttpClient(conf);
        this.clusters = new Clusters(innerHttpClient);
        this.brokers = new Brokers(innerHttpClient);
        this.tenants = new Tenants(innerHttpClient);
        this.namespaces = new Namespaces(innerHttpClient);
        this.persistentTopics = new PersistentTopics(innerHttpClient);
        this.nonPersistentTopics = new NonPersistentTopics(innerHttpClient);
    }

    @Override
    public Clusters clusters() {
        return clusters;
    }

    @Override
    public Brokers brokers() {
        return brokers;
    }

    @Override
    public Tenants tenants() {
        return tenants;
    }

    @Override
    public Namespaces namespaces() {
        return namespaces;
    }

    @Override
    public PersistentTopics persistentTopics() {
        return persistentTopics;
    }

    @Override
    public NonPersistentTopics nonPersistentTopics() {
        return nonPersistentTopics;
    }
}
