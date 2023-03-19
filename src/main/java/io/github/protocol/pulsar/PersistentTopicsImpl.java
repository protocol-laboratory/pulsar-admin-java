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
import java.util.Map;

public class PersistentTopicsImpl implements Topics {

    protected final InnerHttpClient httpClient;

    private static final String BASE_URL_PERSISTENT_DOMAIN = UrlConst.BASE_URL_V2 + "/persistent";

    public PersistentTopicsImpl(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getDomainBaseUrl() {
        return BASE_URL_PERSISTENT_DOMAIN;
    }

    @Override
    public void createPartitionedTopic(String tenant, String namespace, String encodedTopic, int numPartitions,
                                       boolean createLocalTopicOnly) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.PARTITIONS);
        try {
            HttpResponse<String> response = httpClient.put(url, numPartitions, "createLocalTopicOnly",
                    String.valueOf(createLocalTopicOnly));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to create partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePartitionedTopic(String tenant, String namespace, String encodedTopic, boolean force,
                                       boolean authoritative) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.PARTITIONS);
        try {
            HttpResponse<String> response = httpClient.delete(url, "force", String.valueOf(force),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void updatePartitionedTopic(String tenant, String namespace, String encodedTopic,
                                       boolean updateLocalTopicOnly, boolean authoritative,
                                       boolean force, int numPartitions) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.PARTITIONS);
        try {
            HttpResponse<String> response = httpClient.post(url, numPartitions, "updateLocalTopicOnly",
                    String.valueOf(updateLocalTopicOnly),
                    "authoritative", String.valueOf(authoritative),
                    "force", String.valueOf(force));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to update partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public PartitionedTopicMetadata getPartitionedMetadata(String tenant, String namespace, String encodedTopic,
                                                           boolean checkAllowAutoCreation, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(),
                tenant, namespace, encodedTopic, UrlConst.PARTITIONS);
        try {
            HttpResponse<String> response = httpClient.get(url,
                    "checkAllowAutoCreation", String.valueOf(checkAllowAutoCreation),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to update partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), PartitionedTopicMetadata.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void createNonPartitionedTopic(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                          Map<String, String> properties) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic);
        try {
            HttpResponse<String> response = httpClient.put(url, properties,
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to create non-partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void deleteTopic(String tenant, String namespace, String encodedTopic, boolean force, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic);
        try {
            HttpResponse<String> response = httpClient.delete(url,
                    "force", String.valueOf(force),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete non-partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public List<String> getList(String tenant, String namespace, String bundle, boolean includeSystemTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s", getDomainBaseUrl(), tenant, namespace);
        try {
            HttpResponse<String> response;
            if (bundle != null) {
                response = httpClient.get(url,
                        "bundle", bundle,
                        "includeSystemTopic", String.valueOf(includeSystemTopic));
            } else {
                response = httpClient.get(url,
                        "includeSystemTopic", String.valueOf(includeSystemTopic));
            }
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get list of non-partitioned-topics "
                                        + "under namespace %s/%s, status code %s, body : %s",
                                tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public List<String> getPartitionedTopicList(String tenant, String namespace, boolean includeSystemTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, UrlConst.PARTITIONED);
        try {
            HttpResponse<String> response = httpClient.get(url, "includeSystemTopic",
                    String.valueOf(includeSystemTopic));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get list of partitioned-topics under namespace %s/%s, "
                                        + "status code %s, body : %s",
                                tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void createMissedPartitions(String tenant, String namespace, String encodedTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.CREATE_MISSED_PARTITIONS);
        try {
            HttpResponse<String> response = httpClient.post(url);
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete non-partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void getLastMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException {

    }

    @Override
    public RetentionPolicies getRetention(String tenant, String namespace, String encodedTopic, boolean isGlobal,
                                          boolean applied, boolean authoritative) throws PulsarAdminException {
        return null;
    }

    @Override
    public void setRetention(String tenant, String namespace, String encodedTopic, boolean authoritative,
                             boolean isGlobal, RetentionPolicies retention) throws PulsarAdminException {

    }

    @Override
    public void removeRetention(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException {

    }

    @Override
    public Map<BacklogQuotaType, BacklogQuota> getBacklogQuotaMap(String tenant, String namespace,
                                                                  String encodedTopic, boolean applied,
                                                                  boolean authoritative, boolean isGlobal)
            throws PulsarAdminException {
        return null;
    }

    @Override
    public void setBacklogQuota(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                boolean isGlobal, BacklogQuotaType backlogQuotaType, BacklogQuota backlogQuota)
            throws PulsarAdminException {

    }

    @Override
    public void removeBacklogQuota(String tenant, String namespace, String encodedTopic,
                                   BacklogQuotaType backlogQuotaType, boolean authoritative, boolean isGlobal)
            throws PulsarAdminException {

    }

    @Override
    public PersistentOfflineTopicStats getBacklog(String tenant, String namespace, String encodedTopic,
                                                  boolean authoritative) throws PulsarAdminException {
        return null;
    }

    @Override
    public long getBacklogSizeByMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                          MessageIdImpl messageId) throws PulsarAdminException {
        return 0;
    }

}
