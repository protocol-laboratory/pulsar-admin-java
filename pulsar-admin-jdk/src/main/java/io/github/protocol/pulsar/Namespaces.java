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
