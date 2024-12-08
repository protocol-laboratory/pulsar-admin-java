package io.github.protocol.pulsar.admin.jdk;

public class UrlConst {

    public static final String BASE_URL_V2 = "/admin/v2";

    public static final String CLUSTERS = "/admin/v2" + "/clusters";

    public static final String BROKERS = "/admin/v2" + "/brokers";

    public static final String TENANTS = "/admin/v2" + "/tenants";

    public static final String NAMESPACES = "/admin/v2" + "/namespaces";

    public static final String BACKLOG_QUOTA_MAP = "/backlogQuotaMap";

    public static final String BACKLOG_QUOTA = "/backlogQuota";

    public static final String BACKLOG = "/backlog";

    public static final String BACKLOG_SIZE = "/backlogSize";

    public static final String RETENTION = "/retention";

    public static final String CLEAR_BACKLOG = "/clearBacklog";

    public static final String MESSAGE_TTL = "/messageTTL";

    public static final String COMPACTION_THRESHOLD = "/compactionThreshold";

    public static final String PARTITIONS = "/partitions";

    public static final String PARTITIONED = "/partitioned";

    public static final String CREATE_MISSED_PARTITIONS = "/createMissedPartitions";

    public static final String LAST_MESSAGE_ID = "/lastMessageId";

    public static final String HEALTHCHECK = BROKERS + "/health";
}
