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
