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
import java.util.Map;

public interface Namespaces {

    List<String> getTenantNamespaces(String tenant) throws PulsarAdminException;

    List<String> getTopics(String tenant, String namespace, Mode mode, boolean includeSystemTopic)
            throws PulsarAdminException;

    void createNamespace(String tenant, String namespace) throws PulsarAdminException;

    void deleteNamespace(String tenant, String namespace, boolean force, boolean authoritative)
            throws PulsarAdminException;

    Map<BacklogQuotaType, BacklogQuota> getBacklogQuotaMap(String tenant, String namespace) throws PulsarAdminException;

    void setBacklogQuota(String tenant, String namespace, BacklogQuotaType backlogQuotaType, BacklogQuota backlogQuota)
            throws PulsarAdminException;

    void removeBacklogQuota(String tenant, String namespace, BacklogQuotaType backlogQuotaType)
            throws PulsarAdminException;

    void clearBackLog(String tenant, String namespace, boolean authoritative) throws PulsarAdminException;

    void clearNamespaceBacklogForSubscription(String tenant, String namespace, String subscription,
                                              boolean authoritative) throws PulsarAdminException;

    RetentionPolicies getRetention(String tenant, String namespace) throws PulsarAdminException;

    void setRetention(String tenant, String namespace, RetentionPolicies retentionPolicies) throws PulsarAdminException;

    void removeRetention(String tenant, String namespace, RetentionPolicies retentionPolicies)
            throws PulsarAdminException;

    Integer getNamespaceMessageTTL(String tenant, String namespace) throws PulsarAdminException;

    void setNamespaceMessageTTL(String tenant, String namespace, int messageTTL) throws PulsarAdminException;

    void removeNamespaceMessageTTL(String tenant, String namespace) throws PulsarAdminException;

    Long getCompactionThreshold(String tenant, String namespace) throws PulsarAdminException;

    void setCompactionThreshold(String tenant, String namespace, long newThreshold) throws PulsarAdminException;

    void deleteCompactionThreshold(String tenant, String namespace) throws PulsarAdminException;

}
