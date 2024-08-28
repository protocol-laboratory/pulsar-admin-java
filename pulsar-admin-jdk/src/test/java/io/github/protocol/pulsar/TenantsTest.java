package io.github.protocol.pulsar;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TenantsTest {

    protected static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    protected static final String CLUSTER_STANDALONE = "standalone";

    protected static PulsarAdmin pulsarAdmin;

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
    public void tenantsTest() throws PulsarAdminException {
        String tenantName = RandomUtil.randomString();
        TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
                .adminRoles(new HashSet<>(0))
                .allowedClusters(Set.of(CLUSTER_STANDALONE)).build();
        TenantInfo updatedTenantInfo = (new TenantInfo.TenantInfoBuilder())
                .adminRoles(Set.of("test"))
                .allowedClusters(Set.of("global"))
                .build();
        pulsarAdmin.tenants().createTenant(tenantName, initialTenantInfo);
        Assertions.assertEquals(List.of(tenantName, "public", "pulsar"), pulsarAdmin.tenants().getTenants());
        Assertions.assertEquals(initialTenantInfo, pulsarAdmin.tenants().getTenantAdmin(tenantName));
        pulsarAdmin.tenants().updateTenant(tenantName, updatedTenantInfo);
        Assertions.assertEquals(updatedTenantInfo, pulsarAdmin.tenants().getTenantAdmin(tenantName));
        pulsarAdmin.tenants().deleteTenant(tenantName, false);
        Assertions.assertEquals(List.of("public", "pulsar"), pulsarAdmin.tenants().getTenants());
    }

}
