/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
        Assertions.assertEquals(pulsarAdmin.tenants().getTenants(), List.of(tenantName, "public", "pulsar"));
        Assertions.assertEquals(pulsarAdmin.tenants().getTenantAdmin(tenantName), initialTenantInfo);
        pulsarAdmin.tenants().updateTenant(tenantName, updatedTenantInfo);
        Assertions.assertEquals(pulsarAdmin.tenants().getTenantAdmin(tenantName), updatedTenantInfo);
        pulsarAdmin.tenants().deleteTenant(tenantName, false);
        Assertions.assertEquals(pulsarAdmin.tenants().getTenants(), List.of("public", "pulsar"));
    }

}
