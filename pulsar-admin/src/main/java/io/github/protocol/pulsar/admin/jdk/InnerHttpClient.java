package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.openfacade.http.HttpClient;
import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.HttpClientEngine;
import io.github.openfacade.http.HttpClientFactory;
import io.github.openfacade.http.HttpMethod;
import io.github.openfacade.http.HttpRequest;
import io.github.openfacade.http.HttpResponse;
import io.github.openfacade.http.HttpSchema;
import io.github.openfacade.http.TlsConfig;
import io.github.openfacade.http.UrlBuilder;
import io.github.protocol.pulsar.admin.api.Configuration;
import io.github.protocol.pulsar.admin.common.JacksonService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InnerHttpClient {
    private final HttpClient client;

    private UrlBuilder templateUrlBuilder;

    public InnerHttpClient(Configuration conf) {
        HttpClientConfig.Builder clientConfigBuilder = new HttpClientConfig.Builder();
        clientConfigBuilder.engine(HttpClientEngine.Java);
        if (conf.tlsEnabled) {
            TlsConfig.Builder tlsConfigBuilder = new TlsConfig.Builder();
            io.github.protocol.pulsar.admin.api.TlsConfig tlsConfig = conf.tlsConfig;
            tlsConfigBuilder.cipherSuites(tlsConfig.cipherSuites);
            tlsConfigBuilder.hostnameVerifyDisabled(tlsConfig.hostnameVerifyDisabled);
            tlsConfigBuilder.keyStore(tlsConfig.keyStorePath, tlsConfig.keyStorePassword);
            tlsConfigBuilder.trustStore(tlsConfig.trustStorePath, tlsConfig.trustStorePassword);
            tlsConfigBuilder.verifyDisabled(tlsConfig.verifyDisabled);
            clientConfigBuilder.tlsConfig(tlsConfigBuilder.build());
        }
        this.client = HttpClientFactory.createHttpClient(clientConfigBuilder.build());
        templateUrlBuilder = new UrlBuilder();
        templateUrlBuilder.setHttpSchema(conf.tlsEnabled ? HttpSchema.HTTPS : HttpSchema.HTTP).setHost(conf.host)
                          .setPort(conf.port);
    }

    public HttpResponse post(String url) throws IOException, InterruptedException, ExecutionException {
        return this.innerPost(url, new byte[0]);
    }

    public HttpResponse post(String url, Object body) throws IOException, InterruptedException, ExecutionException {
        return this.innerPost(url, objectToBytes(body));
    }

    public HttpResponse post(String url, Object body, String... params)
        throws IOException, ExecutionException, InterruptedException {
        return this.innerPost(url, objectToBytes(body), params);
    }

    private HttpResponse innerPost(String url, byte[] body, String... params)
        throws InterruptedException, ExecutionException {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        HttpRequest request = new HttpRequest(concatUrlWithParams(url, params), HttpMethod.POST, headers, body);
        return client.send(request).get();
    }

    public HttpResponse put(String url) throws IOException, InterruptedException, ExecutionException {
        return this.innerPut(url, new byte[0]);
    }

    public HttpResponse put(String url, Object body) throws IOException, InterruptedException, ExecutionException {
        return this.innerPut(url, objectToBytes(body));
    }

    public HttpResponse put(String url, Object body, String... params)
        throws IOException, InterruptedException, ExecutionException {
        return this.innerPut(url, objectToBytes(body), params);
    }

    private HttpResponse innerPut(String url, byte[] body, String... params)
        throws InterruptedException, ExecutionException {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        HttpRequest request = new HttpRequest(concatUrlWithParams(url, params), HttpMethod.PUT, headers, body);
        return client.send(request).get();
    }

    public HttpResponse delete(String url, String... params)
        throws IOException, InterruptedException, ExecutionException {
        HttpRequest request = new HttpRequest(concatUrlWithParams(url, params), HttpMethod.DELETE);
        return client.send(request).get();
    }

    public HttpResponse get(String url, String... params) throws IOException, InterruptedException, ExecutionException {
        HttpRequest request = new HttpRequest(concatUrlWithParams(url, params), HttpMethod.GET);
        return client.send(request).get();
    }

    private List<UrlBuilder.Param> convertListToParams(String... requestParams) {
        if (requestParams.length % 2 != 0) {
            throw new IllegalArgumentException("params list length cannot be odd");
        }
        List<UrlBuilder.Param> queryParams = new ArrayList<>();
        for (int i = 0; i < requestParams.length; i = i + 2) {
            queryParams.add(new UrlBuilder.Param(requestParams[i], requestParams[i + 1]));
        }
        return queryParams;
    }

    private byte[] objectToBytes(Object obj) throws JsonProcessingException {
        return obj == null ? new byte[0] : JacksonService.toBytes(obj);
    }

    private String concatUrlWithParams(String url, String... params) {
        UrlBuilder urlBuilder = templateUrlBuilder.duplicate();
        urlBuilder.setPath(url);
        if (params != null && params.length > 0) {
            urlBuilder.setQueryParams(convertListToParams(params));
        }
        return urlBuilder.build();
    }
}
