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

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class InnerHttpClient {
    private final Configuration conf;

    private final HttpClient client;

    private final String httpPrefix;

    public InnerHttpClient(Configuration conf) {
        this.conf = conf;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.httpPrefix = "http://" + conf.getHost() + ":" + conf.getPort();
    }

    public HttpResponse<String> get(String url) throws IOException, InterruptedException {
        return this.get(url, new String[0]);
    }

    public HttpResponse<String> get(String url, String... requestParams)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url, requestParams))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String url, Object body, String... params)
            throws IOException, InterruptedException {
        return this.post(url, objectToString(body), params);
    }

    public HttpResponse<String> post(String url, String body, String... params)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url, params))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String url) throws IOException, InterruptedException {
        return this.post(url, "");
    }

    public HttpResponse<String> put(String url) throws IOException, InterruptedException {
        return this.put(url, "");
    }

    public HttpResponse<String> put(String url, Object body, String... params)
            throws IOException, InterruptedException {
        return this.put(url, objectToString(body), params);
    }

    public HttpResponse<String> put(String url, String body, String... params)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url, params))
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(String url, String... requestParams)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url, requestParams))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI getUri(String url, String... params) {
        return URI.create(this.httpPrefix + url + mapToParams(params));
    }

    static String mapToParams(String... requestParams) {
        if (requestParams.length % 2 != 0) {
            throw new IllegalArgumentException("params list length cannot be odd");
        }
        if (requestParams.length == 0) {
            return "";
        }
        StringBuilder res = new StringBuilder("?");
        res.append(requestParams[0]);
        res.append('=');
        res.append(requestParams[1]);
        for (int i = 2; i < requestParams.length; ) {
            res.append('&');
            res.append(encode(requestParams[i++]));
            res.append('=');
            res.append(encode(requestParams[i++]));
        }
        return res.toString();
    }

    private String objectToString(Object obj) throws JsonProcessingException {
        return obj == null ? "" : JacksonService.toJson(obj);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
