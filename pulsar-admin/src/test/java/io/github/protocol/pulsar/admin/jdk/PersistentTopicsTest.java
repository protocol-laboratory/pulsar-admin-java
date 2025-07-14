package io.github.protocol.pulsar.admin.jdk;

import com.google.common.collect.ImmutableMap;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class PersistentTopicsTest extends BaseTest {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void nonPartitionedTopicsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void createMissedPartitionsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void getLastMessageIdTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getLastMessageId(tenant, namespace, topic, false));
        String partitionedTopic = RandomUtil.randomString();
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, partitionedTopic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getLastMessageId(tenant, namespace, topic, false));
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void backLogQuotaTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void retentionTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void getPartitionedStatsTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertNotNull(pulsarAdmin.persistentTopics().getPartitionedStats(tenant, namespace, topic, false));
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void subscriptionTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        String subscriptionNameLatest = RandomUtil.randomString();
        String subscriptionNameEarliest = RandomUtil.randomString();

        // Create namespace and topic
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);

        // Create subscription with message ID latest and earliest
        pulsarAdmin.persistentTopics()
                   .createSubscription(tenant, namespace, topic, subscriptionNameLatest, false, false,
                       SubscriptionMessageId.latest());
        pulsarAdmin.persistentTopics()
                   .createSubscription(tenant, namespace, topic, subscriptionNameEarliest, false, false,
                       SubscriptionMessageId.earliest());

        // Verify subscription was created
        List<String> subscriptions = pulsarAdmin.persistentTopics().getSubscriptions(tenant, namespace, topic, false);
        Assertions.assertTrue(subscriptions.contains(subscriptionNameEarliest),
            "Should contain subscription created with message ID");
        Assertions.assertTrue(subscriptions.contains(subscriptionNameLatest),
            "Should contain subscription created with message ID");

        // test create subscription invalid
        Assertions.assertThrows(PulsarAdminException.class,
            () -> pulsarAdmin.persistentTopics().createSubscription(tenant, namespace, topic, "", false, false, null));

        // test get subscription, topic invalid
        Assertions.assertThrows(PulsarAdminException.class,
            () -> pulsarAdmin.persistentTopics().getSubscriptions(tenant, namespace, "", false));

        // test delete subscription invalid
        Assertions.assertThrows(PulsarAdminException.class,
            () -> pulsarAdmin.persistentTopics().deleteSubscription(tenant, namespace, topic, "", true, false));

        // Clean up
        pulsarAdmin.persistentTopics()
                   .deleteSubscription(tenant, namespace, topic, subscriptionNameEarliest, false, false);
        pulsarAdmin.persistentTopics()
                   .deleteSubscription(tenant, namespace, topic, subscriptionNameLatest, false, false);
    }
}
