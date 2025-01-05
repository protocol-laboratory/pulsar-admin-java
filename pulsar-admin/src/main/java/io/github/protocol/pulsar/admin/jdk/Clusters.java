package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.openfacade.http.HttpResponse;
import io.github.protocol.pulsar.admin.common.JacksonService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Clusters {

    private final InnerHttpClient httpClient;

    public Clusters(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<String> getClusters() throws PulsarAdminException {
        try {
            HttpResponse response = httpClient.get(UrlConst.CLUSTERS);
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get list of clusters, "
                                        + "status code %s, body : %s", response.statusCode(), response.body()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {});
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

}
