package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonPersistentTopicsTest {

    private static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    private static final String CLUSTER_STANDALONE = "standalone";

    private static final String tenant = RandomUtil.randomString();

    private static PulsarAdmin pulsarAdmin;

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
        pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
                .adminRoles(new HashSet<>(0))
                .allowedClusters(Set.of(CLUSTER_STANDALONE)).build();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void partitionedTopicsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertEquals(List.of(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(2, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().updatePartitionedTopic(tenant, namespace, topic, false, false, false, 3);
        Assertions.assertEquals(List.of(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(3, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().deletePartitionedTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(List.of(),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(
                List.of(),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
    }

    @Test
    public void getPartitionedStatsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.nonPersistentTopics().getPartitionedStats(tenant, namespace,
                topic, false));
    }

    @Test
    public void nonPartitionedTopicsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertEquals(
                List.of(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.nonPersistentTopics().deleteTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(
                List.of(),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
    }

    @Test
    public void getLastMessageIdTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertThrows(PulsarAdminException.class, () -> pulsarAdmin.nonPersistentTopics().getLastMessageId(
                tenant, namespace, topic, false));
    }
}
