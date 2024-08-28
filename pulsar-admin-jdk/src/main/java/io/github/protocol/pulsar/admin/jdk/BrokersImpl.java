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

import java.net.http.HttpResponse;

public class BrokersImpl implements Brokers {
    private final InnerHttpClient innerHttpClient;

    public BrokersImpl(InnerHttpClient innerHttpClient) {
        this.innerHttpClient = innerHttpClient;
    }

    @Override
    public void healthcheck(TopicVersion topicVersion) throws PulsarAdminException {
        String url = UrlConst.HEALTHCHECK;
        if (topicVersion != null) {
            url += "?topicVersion=" + topicVersion;
        }
        try {
            HttpResponse<String> httpResponse = innerHttpClient.get(url);
            if (httpResponse.statusCode() != 200) {
                throw new PulsarAdminException("healthcheck failed, status code: " + httpResponse.statusCode(),
                        httpResponse.statusCode());
            }
            if (!httpResponse.body().equals("ok")) {
                throw new PulsarAdminException("healthcheck failed, body: " + httpResponse.body());
            }
        } catch (Exception e) {
            throw new PulsarAdminException(e);
        }
    }
}
