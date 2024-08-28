package io.github.protocol.pulsar.admin.jdk;

import java.util.List;

public interface Tenants {

    void createTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException;

    void deleteTenant(String tenant, boolean force) throws PulsarAdminException;

    void updateTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException;

    TenantInfo getTenantAdmin(String tenant) throws PulsarAdminException;

    List<String> getTenants() throws PulsarAdminException;

}
