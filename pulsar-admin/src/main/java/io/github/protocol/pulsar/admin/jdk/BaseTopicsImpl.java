package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.openfacade.http.HttpResponse;
import io.github.protocol.pulsar.admin.common.JacksonService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class BaseTopicsImpl {

    protected final InnerHttpClient httpClient;

    public BaseTopicsImpl(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected abstract String getDomainBaseUrl();

    public void createPartitionedTopic(String tenant, String namespace, String encodedTopic, int numPartitions,
                                       boolean createLocalTopicOnly) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic, "/partitions");
        try {
            HttpResponse response = httpClient.put(url, numPartitions, "createLocalTopicOnly",
                    String.valueOf(createLocalTopicOnly));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to create partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePartitionedTopic(String tenant, String namespace, String encodedTopic, boolean force,
                                       boolean authoritative) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic, "/partitions");
        try {
            HttpResponse response = httpClient.delete(url, "force", String.valueOf(force),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void updatePartitionedTopic(String tenant, String namespace, String encodedTopic,
                                       boolean updateLocalTopicOnly, boolean authoritative,
                                       boolean force, int numPartitions) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic, "/partitions");
        try {
            HttpResponse response = httpClient.post(url, numPartitions, "updateLocalTopicOnly",
                    String.valueOf(updateLocalTopicOnly),
                    "authoritative", String.valueOf(authoritative),
                    "force", String.valueOf(force));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to update partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public PartitionedTopicStats getPartitionedStats(String tenant, String namespace, String encodedTopic,
                                                           boolean perPartition)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace,
                encodedTopic, "/partitioned-stats");
        try {
            HttpResponse response = httpClient.get(url, "perPartition",
                    Arrays.toString(new Object[] {perPartition}), "getPreciseBacklog",
                    Arrays.toString(new Object[] {false}), "subscriptionBacklogSize",
                    Arrays.toString(new Object[] {false}),
                    "getEarliestTimeInBacklog", Arrays.toString(new Object[] {false}));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get partitioned stats of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), PartitionedTopicStats.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public PartitionedTopicMetadata getPartitionedMetadata(String tenant, String namespace, String encodedTopic,
                                                           boolean checkAllowAutoCreation, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic, "/partitions");
        try {
            HttpResponse response = httpClient.get(url,
                    "checkAllowAutoCreation", String.valueOf(checkAllowAutoCreation),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to update partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), PartitionedTopicMetadata.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void createNonPartitionedTopic(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                          Map<String, String> properties) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic);
        try {
            HttpResponse response = httpClient.put(url, properties,
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to create non-partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void deleteTopic(String tenant, String namespace, String encodedTopic, boolean force, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic);
        try {
            HttpResponse response = httpClient.delete(url,
                    "force", String.valueOf(force),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete non-partitioned topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public List getList(String tenant, String namespace, String bundle, boolean includeSystemTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s", getDomainBaseUrl(), tenant, namespace);
        try {
            HttpResponse response;
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
                                tenant, namespace, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public List getPartitionedTopicList(String tenant, String namespace, boolean includeSystemTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, UrlConst.PARTITIONED);
        try {
            HttpResponse response = httpClient.get(url, "includeSystemTopic",
                    String.valueOf(includeSystemTopic));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get list of partitioned-topics under namespace %s/%s, "
                                        + "status code %s, body : %s",
                                tenant, namespace, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void createMissedPartitions(String tenant, String namespace, String encodedTopic)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.CREATE_MISSED_PARTITIONS);
        try {
            HttpResponse response = httpClient.post(url);
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to create missing partitions for topic %s/%s/%s, "
                                        + "status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public MessageIdImpl getLastMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.LAST_MESSAGE_ID);
        try {
            HttpResponse response = httpClient.get(url, "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get last message id of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), MessageIdImpl.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public RetentionPolicies getRetention(String tenant, String namespace, String encodedTopic, boolean isGlobal,
                                          boolean applied, boolean authoritative) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.RETENTION);
        try {
            HttpResponse response = httpClient.get(url,
                    "isGlobal", String.valueOf(isGlobal),
                    "applied", String.valueOf(applied),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get retention of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), RetentionPolicies.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void setRetention(String tenant, String namespace, String encodedTopic, boolean authoritative,
                             boolean isGlobal, RetentionPolicies retention) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.RETENTION);
        try {
            HttpResponse response = httpClient.post(url, retention,
                    "authoritative", String.valueOf(authoritative),
                    "isGlobal", String.valueOf(isGlobal));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to set retention for topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void removeRetention(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.RETENTION);
        try {
            HttpResponse response = httpClient.delete(url, "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to delete retention of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public Map<BacklogQuotaType, BacklogQuota> getBacklogQuotaMap(String tenant, String namespace,
                                                                  String encodedTopic, boolean applied,
                                                                  boolean authoritative, boolean isGlobal)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.BACKLOG_QUOTA_MAP);
        try {
            HttpResponse response = httpClient.get(url,
                    "applied", String.valueOf(applied),
                    "authoritative", String.valueOf(authoritative),
                    "isGlobal", String.valueOf(isGlobal));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get backlog quota map of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<Map<BacklogQuotaType, BacklogQuota>>() {
            });
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void setBacklogQuota(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                boolean isGlobal, BacklogQuotaType backlogQuotaType, BacklogQuota backlogQuota)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.BACKLOG_QUOTA);
        try {
            HttpResponse response = httpClient.post(url, backlogQuota,
                    "authoritative", String.valueOf(authoritative),
                    "isGlobal", String.valueOf(isGlobal),
                    "backlogQuotaType", String.valueOf(backlogQuotaType));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to set backlog quota for topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void removeBacklogQuota(String tenant, String namespace, String encodedTopic,
                                   BacklogQuotaType backlogQuotaType, boolean authoritative, boolean isGlobal)
            throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.BACKLOG_QUOTA);
        try {
            HttpResponse response = httpClient.delete(url,
                    "backlogQuotaType", String.valueOf(backlogQuotaType),
                    "authoritative", String.valueOf(authoritative),
                    "isGlobal", String.valueOf(isGlobal));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to remove backlog quota of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public PersistentOfflineTopicStats getBacklog(String tenant, String namespace, String encodedTopic,
                                                  boolean authoritative) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.BACKLOG);
        try {
            HttpResponse response = httpClient.get(url, "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get backlog of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), PersistentOfflineTopicStats.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public long getBacklogSizeByMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                          MessageIdImpl messageId) throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
                UrlConst.BACKLOG_SIZE);
        try {
            HttpResponse response = httpClient.put(url, messageId, "authoritative",
                    String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get backlog size of topic %s/%s/%s, status code %s, body : %s",
                                tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toObject(response.body(), Long.class);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }
}
