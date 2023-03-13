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

import java.util.List;

public interface Tenants {

    void createTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException;

    void deleteTenant(String tenant, boolean force) throws PulsarAdminException;

    void updateTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException;

    TenantInfo getTenantAdmin(String tenant) throws PulsarAdminException;

    List<String> getTenants() throws PulsarAdminException;

}
