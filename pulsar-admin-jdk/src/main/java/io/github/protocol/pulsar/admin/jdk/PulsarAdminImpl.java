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

public class PulsarAdminImpl implements PulsarAdmin {

    private final Clusters clusters;

    private final Brokers brokers;

    private final Tenants tenants;

    private final Namespaces namespaces;

    private final PersistentTopicsImpl persistentTopics;

    private final NonPersistentTopicsImpl nonPersistentTopics;

    PulsarAdminImpl(Configuration conf) {
        InnerHttpClient innerHttpClient = new InnerHttpClient(conf);
        this.clusters = new ClustersImpl(innerHttpClient);
        this.brokers = new BrokersImpl(innerHttpClient);
        this.tenants = new TenantsImpl(innerHttpClient);
        this.namespaces = new NamespacesImpl(innerHttpClient);
        this.persistentTopics = new PersistentTopicsImpl(innerHttpClient);
        this.nonPersistentTopics = new NonPersistentTopicsImpl(innerHttpClient);
    }

    @Override
    public Clusters clusters() {
        return clusters;
    }

    @Override
    public Brokers brokers() {
        return brokers;
    }

    @Override
    public Tenants tenants() {
        return tenants;
    }

    @Override
    public Namespaces namespaces() {
        return namespaces;
    }

    @Override
    public PersistentTopicsImpl persistentTopics() {
        return persistentTopics;
    }

    @Override
    public NonPersistentTopicsImpl nonPersistentTopics() {
        return nonPersistentTopics;
    }
}
