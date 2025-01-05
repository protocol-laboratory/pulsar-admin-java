package io.github.protocol.pulsar.admin.jdk;

import com.google.common.collect.ImmutableMap;
import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

public class PersistentTopicsTest {

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
            .allowedClusters(new HashSet<>(Arrays.asList(CLUSTER_STANDALONE))).build();
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
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertEquals(Arrays.asList(String.format("persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(2, pulsarAdmin.persistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s-partition-0", tenant, namespace, topic),
                        String.format("persistent://%s/%s/%s-partition-1", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().updatePartitionedTopic(tenant, namespace, topic, false, false, false, 3);
        Assertions.assertEquals(Arrays.asList(String.format("persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(3, pulsarAdmin.persistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s-partition-0", tenant, namespace, topic),
                        String.format("persistent://%s/%s/%s-partition-1", tenant, namespace, topic),
                        String.format("persistent://%s/%s/%s-partition-2", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().deletePartitionedTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(Arrays.asList(),
                pulsarAdmin.persistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(
                Arrays.asList(),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));

    }

    @Test
    public void nonPartitionedTopicsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().deleteTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(
                Arrays.asList(),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
    }

    @Test
    public void createMissedPartitionsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s-partition-0", tenant, namespace, topic),
                        String.format("persistent://%s/%s/%s-partition-1", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().deleteTopic(tenant, namespace, topic + "-partition-1", false, false);
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s-partition-0", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().createMissedPartitions(tenant, namespace, topic);
        Assertions.assertEquals(
                Arrays.asList(String.format("persistent://%s/%s/%s-partition-0", tenant, namespace, topic),
                        String.format("persistent://%s/%s/%s-partition-1", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
    }

    @Test
    public void getLastMessageIdTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getLastMessageId(tenant, namespace, topic, false));
        String partitionedTopic = RandomUtil.randomString();
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, partitionedTopic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getLastMessageId(tenant, namespace, topic, false));
    }

    @Test
    public void backLogQuotaTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        BacklogQuota backlogQuota1 = BacklogQuota.builder()
                .limitSize(RandomUtil.randomLong())
                .limitTime(RandomUtil.randomInt())
                .policy(RetentionPolicy.consumer_backlog_eviction)
                .build();
        BacklogQuota backlogQuota2 = BacklogQuota.builder()
                .limitSize(RandomUtil.randomLong())
                .limitTime(RandomUtil.randomInt())
                .policy(RetentionPolicy.producer_request_hold)
                .build();
        pulsarAdmin.persistentTopics().setBacklogQuota(tenant, namespace, topic, false, false,
                BacklogQuotaType.destination_storage, backlogQuota1);

        //wait for metadata refresh

        Awaitility.await().until(() -> ImmutableMap.of(BacklogQuotaType.destination_storage, backlogQuota1).equals(
            new TreeMap<>(
                pulsarAdmin.persistentTopics().getBacklogQuotaMap(tenant, namespace, topic, false, false, false))));
        pulsarAdmin.persistentTopics()
                   .setBacklogQuota(tenant, namespace, topic, false, false, BacklogQuotaType.message_age,
                       backlogQuota2);

        //wait for metadata refresh

        Awaitility.await().until(
            () -> ImmutableMap.of(BacklogQuotaType.message_age, backlogQuota2, BacklogQuotaType.destination_storage,
                backlogQuota1).equals(new TreeMap<>(
                pulsarAdmin.persistentTopics().getBacklogQuotaMap(tenant, namespace, topic, false, false, false))));
        pulsarAdmin.persistentTopics().removeBacklogQuota(tenant, namespace, topic,
                BacklogQuotaType.destination_storage, false, false);
        Assertions.assertEquals(ImmutableMap.of(BacklogQuotaType.message_age, backlogQuota2),
                pulsarAdmin.persistentTopics().getBacklogQuotaMap(tenant, namespace, topic, false,
                        false, false));
        Assertions.assertEquals(0L, pulsarAdmin.persistentTopics().getBacklogSizeByMessageId(tenant, namespace,
                topic, false, pulsarAdmin.persistentTopics().getLastMessageId(tenant, namespace, topic, false)));
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getBacklog(tenant, namespace, topic, false));
    }

    @Test
    public void retentionTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        BacklogQuota backlogQuota = BacklogQuota.builder()
                .limitSize(RandomUtil.randomPositiveLong())
                .limitTime(RandomUtil.randomPositiveInt())
                .policy(RetentionPolicy.consumer_backlog_eviction)
                .build();
        pulsarAdmin.persistentTopics().setBacklogQuota(tenant, namespace, topic, false, false,
                BacklogQuotaType.destination_storage, backlogQuota);

        RetentionPolicies retentionPolicies = RetentionPolicies.builder()
                .retentionSizeInMB(RandomUtil.randomNegativeInt())
                .retentionTimeInMinutes(RandomUtil.randomNegativeInt())
                .build();
        pulsarAdmin.persistentTopics().setRetention(tenant, namespace, topic, false, false, retentionPolicies);

        //wait for metadata refresh

        Awaitility.await().until(() -> retentionPolicies.equals(pulsarAdmin.persistentTopics().getRetention(
                tenant, namespace, topic, false, false, false)));

        pulsarAdmin.persistentTopics().removeRetention(tenant, namespace, topic, false);
        Assertions.assertNull(pulsarAdmin.persistentTopics().getRetention(tenant, namespace, topic,
                false, false, false));
    }

    @Test
    public void getPartitionedStatsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getPartitionedStats(tenant, namespace, topic, false));
    }

}
