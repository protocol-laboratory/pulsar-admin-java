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

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class TenantsImpl implements Tenants {

    private final InnerHttpClient httpClient;

    public TenantsImpl(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void createTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.put(
                            String.format("%s/%s", UrlConst.TENANTS, tenant), tenantInfo);
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to create tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void deleteTenant(String tenant, boolean force) throws PulsarAdminException {
        try {
            HttpResponse<String> response =
                    httpClient.delete(String.format("%s/%s", UrlConst.TENANTS, tenant), "force", String.valueOf(force));
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to create tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void updateTenant(String tenant, TenantInfo tenantInfo) throws PulsarAdminException {
        try {
            HttpResponse<String> response =
                    httpClient.post(
                            String.format("%s/%s", UrlConst.TENANTS, tenant), tenantInfo);
            if (response.statusCode() != 204){
                throw new PulsarAdminException(String.format("failed to update tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public TenantInfo getTenantAdmin(String tenant) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                            String.format("%s/%s", UrlConst.TENANTS, tenant));
            if (response.statusCode() != 200){
                throw new PulsarAdminException(String.format("failed to get tenant %s, status code %s, body : %s",
                        tenant, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), TenantInfo.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public List<String> getTenants() throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(UrlConst.TENANTS);
            if (response.statusCode() != 200){
                throw new PulsarAdminException(String.format("failed to get list of tenant, status code %s, body : %s",
                        response.statusCode(), response.body()));
            }
            return JacksonService.toList(response.body(), new TypeReference<List<String>>(){});
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }
}
