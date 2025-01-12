package io.github.protocol.pulsar.admin.jdk;

import com.google.common.collect.ImmutableMap;
import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

public class NamespacesTest {

    private static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    private static final String CLUSTER_STANDALONE = "standalone";

    private static final TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
            .adminRoles(new HashSet<>(0))
            .allowedClusters(new HashSet<>(Arrays.asList(CLUSTER_STANDALONE))).build();

    private static PulsarAdmin pulsarAdmin;

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
        pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void namespaceTest() throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        Assertions.assertEquals(Arrays.asList(), pulsarAdmin.namespaces()
                                                    .getTopics(tenant, namespace, Mode.PERSISTENT, true));
        Assertions.assertEquals(Arrays.asList(), pulsarAdmin.namespaces()
                .getTopics(tenant, namespace, Mode.NON_PERSISTENT, true));
        Assertions.assertEquals(Arrays.asList(), pulsarAdmin.namespaces()
                .getTopics(tenant, namespace, Mode.ALL, false));
        Assertions.assertEquals(
            Arrays.asList(tenant + "/" + namespace), pulsarAdmin.namespaces().getTenantNamespaces(tenant));
        pulsarAdmin.namespaces().deleteNamespace(tenant, namespace, false, false);
        Assertions.assertEquals(Arrays.asList(), pulsarAdmin.namespaces().getTenantNamespaces(tenant));
    }

    @Test
    public void namespacesBacklogQuotaTest() throws PulsarAdminException, InterruptedException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
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
        pulsarAdmin.namespaces().setBacklogQuota(tenant, namespace, BacklogQuotaType.message_age, backlogQuota1);
        pulsarAdmin.namespaces().setBacklogQuota(tenant, namespace,
                BacklogQuotaType.destination_storage, backlogQuota2);
        Assertions.assertEquals(ImmutableMap.of(
                        BacklogQuotaType.destination_storage, backlogQuota2,
                        BacklogQuotaType.message_age, backlogQuota1),
                new TreeMap<>(pulsarAdmin.namespaces().getBacklogQuotaMap(tenant, namespace)));
        backlogQuota1.setPolicy(RetentionPolicy.producer_exception);
        pulsarAdmin.namespaces().setBacklogQuota(tenant, namespace, BacklogQuotaType.message_age, backlogQuota1);
        Assertions.assertEquals(ImmutableMap.of(
                        BacklogQuotaType.destination_storage, backlogQuota2,
                        BacklogQuotaType.message_age, backlogQuota1),
                new TreeMap<>(pulsarAdmin.namespaces().getBacklogQuotaMap(tenant, namespace)));
        pulsarAdmin.namespaces().removeBacklogQuota(tenant, namespace, BacklogQuotaType.message_age);
        Assertions.assertEquals(ImmutableMap.of(BacklogQuotaType.destination_storage, backlogQuota2),
                new TreeMap<>(pulsarAdmin.namespaces().getBacklogQuotaMap(tenant, namespace)));
    }

    @Test
    public void namespacesClearBacklogTest() throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.namespaces().clearBackLog(tenant, namespace, false);
        pulsarAdmin.namespaces().clearNamespaceBacklogForSubscription(
                tenant, namespace, RandomUtil.randomString(), false);
    }

    @Test
    public void namespacesRetentionTest() throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        RetentionPolicies retentionPolicies = RetentionPolicies.builder()
                .retentionSizeInMB(RandomUtil.randomPositiveInt())
                .retentionTimeInMinutes(RandomUtil.randomPositiveInt())
                .build();
        pulsarAdmin.namespaces().setRetention(tenant, namespace, retentionPolicies);
        Assertions.assertEquals(retentionPolicies, pulsarAdmin.namespaces().getRetention(tenant, namespace));
        pulsarAdmin.namespaces().removeRetention(tenant, namespace, retentionPolicies);
        Assertions.assertNull(pulsarAdmin.namespaces().getRetention(tenant, namespace));
    }

    @Test
    public void namespacesMessageTTLTest() throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        int messageTTL = RandomUtil.randomPositiveInt();
        pulsarAdmin.namespaces().setNamespaceMessageTTL(tenant, namespace, messageTTL);
        Assertions.assertEquals(messageTTL, pulsarAdmin.namespaces().getNamespaceMessageTTL(tenant, namespace));
        pulsarAdmin.namespaces().removeNamespaceMessageTTL(tenant, namespace);
        Assertions.assertNull(pulsarAdmin.namespaces().getNamespaceMessageTTL(tenant, namespace));
    }

    @Test
    public void namespacesCompactionThresholdTest() throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        long newThreshold = RandomUtil.randomPositiveInt();
        pulsarAdmin.namespaces().setCompactionThreshold(tenant, namespace, newThreshold);
        Assertions.assertEquals(newThreshold, pulsarAdmin.namespaces().getCompactionThreshold(tenant, namespace));
        pulsarAdmin.namespaces().deleteCompactionThreshold(tenant, namespace);
        Assertions.assertNull(pulsarAdmin.namespaces().getCompactionThreshold(tenant, namespace));
    }

}
