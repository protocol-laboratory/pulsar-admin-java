package io.github.protocol.pulsar.admin.jdk;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

public class NamespacesTest extends BaseTest {
    private static final String CLUSTER_STANDALONE = "standalone";

    private static final TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
            .adminRoles(new HashSet<>(0))
            .allowedClusters(new HashSet<>(Arrays.asList(CLUSTER_STANDALONE))).build();

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespaceTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespacesBacklogQuotaTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException, InterruptedException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespacesClearBacklogTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        String tenant = RandomUtil.randomString();
        String namespace = RandomUtil.randomString();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.namespaces().clearBackLog(tenant, namespace, false);
        pulsarAdmin.namespaces().clearNamespaceBacklogForSubscription(
                tenant, namespace, RandomUtil.randomString(), false);
    }

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespacesRetentionTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespacesMessageTTLTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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

    @ParameterizedTest
    @MethodSource("providePulsarAdmins")
    public void namespacesCompactionThresholdTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
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
