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

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonPersistentTopicsTest {

    private static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    private static final String CLUSTER_STANDALONE = "standalone";

    private static final String tenant = RandomUtil.randomString();

    private static PulsarAdmin pulsarAdmin;

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
        pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        TenantInfo initialTenantInfo = (new TenantInfo.TenantInfoBuilder())
                .adminRoles(new HashSet<>(0))
                .allowedClusters(Set.of(CLUSTER_STANDALONE)).build();
        pulsarAdmin.tenants().createTenant(tenant, initialTenantInfo);
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void partitionedTopicsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.nonPersistentTopics().createPartitionedTopic(tenant, namespace, topic, 2, false);
        Assertions.assertEquals(List.of(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(2, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().updatePartitionedTopic(tenant, namespace, topic, false, false, false, 3);
        Assertions.assertEquals(List.of(String.format("non-persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(3, pulsarAdmin.nonPersistentTopics().getPartitionedMetadata(tenant, namespace,
                topic, false, false).getPartitions());
        pulsarAdmin.nonPersistentTopics().deletePartitionedTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(List.of(),
                pulsarAdmin.nonPersistentTopics().getPartitionedTopicList(tenant, namespace, false));
        Assertions.assertEquals(
                List.of(),
                pulsarAdmin.nonPersistentTopics().getList(tenant, namespace, null, false));
    }

    @Test
    public void nonPartitionedTopicsTest() throws PulsarAdminException {
        String namespace = RandomUtil.randomString();
        String topic = RandomUtil.randomString();
        pulsarAdmin.namespaces().createNamespace(tenant, namespace);
        pulsarAdmin.persistentTopics().createNonPartitionedTopic(tenant, namespace, topic, false, null);
        Assertions.assertEquals(
                List.of(String.format("persistent://%s/%s/%s", tenant, namespace, topic)),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
        pulsarAdmin.persistentTopics().deleteTopic(tenant, namespace, topic, false, false);
        Assertions.assertEquals(
                List.of(),
                pulsarAdmin.persistentTopics().getList(tenant, namespace, null, false));
    }

}
