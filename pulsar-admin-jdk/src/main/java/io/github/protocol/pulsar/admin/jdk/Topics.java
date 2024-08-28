package io.github.protocol.pulsar.admin.jdk;

import java.util.List;
import java.util.Map;

public interface Topics {

    void createPartitionedTopic(String tenant, String namespace, String encodedTopic, int numPartitions,
                                boolean createLocalTopicOnly) throws PulsarAdminException;

    void deletePartitionedTopic(String tenant, String namespace, String encodedTopic, boolean force,
                                boolean authoritative) throws PulsarAdminException;

    void updatePartitionedTopic(String tenant, String namespace, String encodedTopic, boolean updateLocalTopicOnly,
                                boolean authoritative, boolean force, int numPartitions) throws PulsarAdminException;

    PartitionedTopicMetadata getPartitionedMetadata(String tenant, String namespace, String encodedTopic,
                                                    boolean checkAllowAutoCreation, boolean authoritative)
            throws PulsarAdminException;

    void createNonPartitionedTopic(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                   Map<String, String> properties) throws PulsarAdminException;

    void deleteTopic(String tenant, String namespace, String encodedTopic, boolean force, boolean authoritative)
            throws PulsarAdminException;

    List<String> getList(String tenant, String namespace, String bundle, boolean includeSystemTopic)
            throws PulsarAdminException;

    List<String> getPartitionedTopicList(String tenant, String namespace, boolean includeSystemTopic)
            throws PulsarAdminException;

    void createMissedPartitions(String tenant, String namespace, String encodedTopic) throws PulsarAdminException;

    MessageIdImpl getLastMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException;

    RetentionPolicies getRetention(String tenant, String namespace, String encodedTopic,
                                   boolean isGlobal, boolean applied, boolean authoritative)
            throws PulsarAdminException;

    void setRetention(String tenant, String namespace, String encodedTopic, boolean authoritative, boolean isGlobal,
                      RetentionPolicies retention) throws PulsarAdminException;

    void removeRetention(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException;

    Map<BacklogQuotaType, BacklogQuota> getBacklogQuotaMap(String tenant, String namespace, String encodedTopic,
                                                           boolean applied, boolean authoritative, boolean isGlobal)
            throws PulsarAdminException;

    void setBacklogQuota(String tenant, String namespace, String encodedTopic, boolean authoritative,
                         boolean isGlobal, BacklogQuotaType backlogQuotaType, BacklogQuota backlogQuota)
            throws PulsarAdminException;

    void removeBacklogQuota(String tenant, String namespace, String encodedTopic, BacklogQuotaType backlogQuotaType,
                            boolean authoritative, boolean isGlobal) throws PulsarAdminException;

    PersistentOfflineTopicStats getBacklog(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException;

    long getBacklogSizeByMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative,
                                   MessageIdImpl messageId) throws PulsarAdminException;

}
