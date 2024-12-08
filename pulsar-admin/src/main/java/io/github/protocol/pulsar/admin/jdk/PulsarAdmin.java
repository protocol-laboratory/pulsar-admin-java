package io.github.protocol.pulsar.admin.jdk;

public interface PulsarAdmin {
    static PulsarAdminBuilder builder() {
        return new PulsarAdminBuilderImpl();
    }

    Clusters clusters();

    Brokers brokers();

    Tenants tenants();

    Namespaces namespaces();

    PersistentTopics persistentTopics();

    NonPersistentTopics nonPersistentTopics();
}
