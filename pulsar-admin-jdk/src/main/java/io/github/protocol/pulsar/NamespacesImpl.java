package io.github.protocol.pulsar;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class NamespacesImpl implements Namespaces {

    private final InnerHttpClient httpClient;

    public NamespacesImpl(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<String> getTenantNamespaces(String tenant) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s", UrlConst.NAMESPACES, tenant));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get namespaces of tenant %s, status code %s, body : %s",
                                tenant, response.statusCode(), response.body()));
            }
            return JacksonService.toList(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public List<String> getTopics(String tenant, String namespace, Mode mode, boolean includeSystemTopic)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s/%s/topics", UrlConst.NAMESPACES, tenant, namespace),
                    "mode", String.valueOf(mode),
                    "includeSystemTopic", String.valueOf(includeSystemTopic));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get topics of namespace %s/%s, status code %s, body : %s",
                                tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toList(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void createNamespace(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.put(
                    String.format("%s/%s/%s", UrlConst.NAMESPACES, tenant, namespace));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                        String.format("failed to get topics of namespace %s/%s, status code %s, body : %s",
                                tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void deleteNamespace(String tenant, String namespace, boolean force, boolean authoritative)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.delete(
                    String.format("%s/%s/%s", UrlConst.NAMESPACES, tenant, namespace),
                    "force", String.valueOf(force),
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to get topics of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public Map<BacklogQuotaType, BacklogQuota> getBacklogQuotaMap(String tenant, String namespace)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.BACKLOG_QUOTA_MAP));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(String.format(
                        "failed to get backlog quota map of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<Map<BacklogQuotaType, BacklogQuota>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void setBacklogQuota(String tenant, String namespace, BacklogQuotaType backlogQuotaType,
                                BacklogQuota backlogQuota) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.post(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.BACKLOG_QUOTA),
                    backlogQuota, "backlogQuotaType", String.valueOf(backlogQuotaType));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to set backlog quota of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void removeBacklogQuota(String tenant, String namespace, BacklogQuotaType backlogQuotaType)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.delete(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.BACKLOG_QUOTA),
                    "backlogQuotaType", String.valueOf(backlogQuotaType));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to delete backlog quota policy of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void clearNamespaceBacklogForSubscription(String tenant, String namespace, String subscription,
                                                     boolean authoritative) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.post(
                    String.format("%s/%s/%s%s/%s", UrlConst.NAMESPACES, tenant, namespace,
                            UrlConst.CLEAR_BACKLOG, subscription),
                    null,
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to clear backlog of namespace %s/%s subscription %s, status code %s, body : %s",
                        tenant, namespace, subscription, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void clearBackLog(String tenant, String namespace, boolean authoritative) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.post(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.CLEAR_BACKLOG),
                    null,
                    "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to clear backlog of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public RetentionPolicies getRetention(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.RETENTION));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(String.format(
                        "failed to get retention of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), RetentionPolicies.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void setRetention(String tenant, String namespace, RetentionPolicies retentionPolicies)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.post(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.RETENTION),
                    retentionPolicies);
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to set retention of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void removeRetention(String tenant, String namespace, RetentionPolicies retentionPolicies)
            throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.delete(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.RETENTION));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to remove retention of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public Integer getNamespaceMessageTTL(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.MESSAGE_TTL));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(String.format(
                        "failed to get messageTTL of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), Integer.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void setNamespaceMessageTTL(String tenant, String namespace, int messageTTL) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.post(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.MESSAGE_TTL),
                    messageTTL);
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to set messageTTL of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void removeNamespaceMessageTTL(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.delete(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.MESSAGE_TTL));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to remove messageTTL of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public Long getCompactionThreshold(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.COMPACTION_THRESHOLD));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(String.format(
                        "failed to get compaction threshold of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
            return JacksonService.toObject(response.body(), Long.class);
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void setCompactionThreshold(String tenant, String namespace, long newThreshold) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.put(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.COMPACTION_THRESHOLD),
                    newThreshold);
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to set compaction threshold of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

    @Override
    public void deleteCompactionThreshold(String tenant, String namespace) throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.delete(
                    String.format("%s/%s/%s%s", UrlConst.NAMESPACES, tenant, namespace, UrlConst.COMPACTION_THRESHOLD));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(String.format(
                        "failed to remove compaction threshold of namespace %s/%s, status code %s, body : %s",
                        tenant, namespace, response.statusCode(), response.body()));
            }
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }
}
