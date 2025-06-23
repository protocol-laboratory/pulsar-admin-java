package io.github.protocol.pulsar.admin.jdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;

public class NonPersistentTopicsTest extends BaseTest{
    private static final String CLUSTER_STANDALONE = "standalone";

    private static final String tenant = RandomUtil.randomString();

    @BeforeAll
    public void setup() throws Exception {
        super.setup();
        // create one tenant is enough
        TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
            .adminRoles(new HashSet<>(0))
            .allowedClusters(new HashSet<>(Arrays.asList(CLUSTER_STANDALONE))).build();
        pulsarAdmins.get(0).tenants().createTenant(tenant, initialTenantInfo);
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void partitionedTopicsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertEquals(Arrays.asList(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(2, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().updatePartitionedTopic(tenant, namespace, topic, false, false, false, 3);
        Assertions.assertEquals(Arrays.asList(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(3, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().deletePartitionedTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(Arrays.asList(),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(
                Arrays.asList(),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void getPartitionedStatsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.nonPersistentTopics().getPartitionedStats(tenant, namespace,
                topic, false));
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void nonPartitionedTopicsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertEquals(
                Arrays.asList(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.nonPersistentTopics().deleteTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(
                Arrays.asList(),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void getLastMessageIdTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertThrows(PulsarAdminException.class, () -> pulsarAdmin.nonPersistentTopics().getLastMessageId(
                tenant, namespace, topic, false));
    }
}
