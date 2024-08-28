package io.github.protocol.pulsar;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class ClustersImpl implements Clusters{

    private final InnerHttpClient httpClient;

    public ClustersImpl(InnerHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<String> getClusters() throws PulsarAdminException {
        try {
            HttpResponse<String> response = httpClient.get(UrlConst.CLUSTERS);
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                        String.format("failed to get list of clusters, "
                                        + "status code %s, body : %s", response.statusCode(), response.body()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException e) {
            throw new PulsarAdminException(e);
        }
    }

}
