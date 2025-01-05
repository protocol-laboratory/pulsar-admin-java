package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

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
            .allowedClusters(new HashSet<>(Arrays.asList(CLUSTER_STANDALONE))).build();
        TenantInfo updatedTenantInfo = (new TenantInfo.TenantInfoBuilder())
                .adminRoles(new HashSet<>(Arrays.asList("test")))
                .allowedClusters(new HashSet<>(Arrays.asList(("global"))))
                .build();
        pulsarAdmin.tenants().createTenant(tenantName, initialTenantInfo);
        Assertions.assertEquals(Arrays.asList(tenantName, "public", "pulsar"), pulsarAdmin.tenants().getTenants());
        Assertions.assertEquals(initialTenantInfo, pulsarAdmin.tenants().getTenantAdmin(tenantName));
        pulsarAdmin.tenants().updateTenant(tenantName, updatedTenantInfo);
        Assertions.assertEquals(updatedTenantInfo, pulsarAdmin.tenants().getTenantAdmin(tenantName));
        pulsarAdmin.tenants().deleteTenant(tenantName, false);
        Assertions.assertEquals(Arrays.asList("public", "pulsar"), pulsarAdmin.tenants().getTenants());
    }

}
